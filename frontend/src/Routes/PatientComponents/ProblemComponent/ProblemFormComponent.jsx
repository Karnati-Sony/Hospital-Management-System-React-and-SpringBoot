import React, { Component } from 'react';
import ProblemService from '../../../services/ProblemService';
import { ErrorMessage, Field, Form, Formik } from "formik";
import Select from 'react-select';
import AlertifyService from '../../../services/AlertifyService';

const STATUS_LABELS = {
  OUT_PATIENT: "Out Patient",
  IN_PATIENT: "In Patient",
  EMERGENCY: "Emergency",
  OPERATION_THEATRE: "Operation Theatre",
  ICU: "ICU"
};

export default class ProblemFormComponent extends Component {

  constructor(props) {
    super(props);

    const patientId = Number(localStorage.getItem("patientId"));

    this.state = {
      pId: patientId,
      problemName: '',
      problemDetail: '',
      problemStatus: 'OUT_PATIENT',

      options: [],
      selectedOption: null
    };
  }

  componentDidMount() {
    this.loadStatus();
  }

  loadStatus() {
    ProblemService.getProblemStatus()
      .then(res => {
        const options = res.data.map(s => ({
          value: s,
          label: STATUS_LABELS[s] || s
        }));

        this.setState({
          options,
          selectedOption: options[0],
          problemStatus: options[0].value
        });
      })
      .catch(() => {
        AlertifyService.alert("Unable to load problem status");
      });
  }

  viewPatient = () => {
    this.props.history.push(`/view-patient/${this.state.pId}`);
  };

  validate(values) {
    let errors = {};
    if (!values.problemName || values.problemName.length < 5) {
      errors.problemName = "Enter at least 5 characters";
    }
    if (!values.problemDetail || values.problemDetail.length < 5) {
      errors.problemDetail = "Enter at least 5 characters";
    }
    return errors;
  }

  // ✅ FINAL CORRECT SAVE METHOD
  addProblem = () => {

    const problem = {
      problemName: this.state.problemName,
      problemDetail: this.state.problemDetail,
      problemStatus: this.state.problemStatus,
      status: 1, // ✅ important if DB column is NOT NULL

      patient: {
        patientid: this.state.pId
      }
    };

    console.log("FINAL PAYLOAD 👉", problem);

    ProblemService.add(problem)
      .then(() => {
        AlertifyService.successMessage("Problem saved successfully");
        this.viewPatient();
      })
      .catch(err => {
        console.error("BACKEND ERROR 👉", err.response?.data || err);
        AlertifyService.alert("Error while saving problem");
      });
  };

  render() {
    return (
      <Formik
        initialValues={this.state}
        enableReinitialize
        validate={this.validate}
        onSubmit={this.addProblem}
      >
        {() => (
          <Form>

            <fieldset className="form-group">
              <label>Problem Name</label>
              <Field
                name="problemName"
                className="form-control"
                onChange={e => this.setState({ problemName: e.target.value })}
              />
              <ErrorMessage name="problemName" component="div" className="text-danger" />
            </fieldset>

            <fieldset className="form-group">
              <label>Problem Detail</label>
              <Field
                name="problemDetail"
                className="form-control"
                onChange={e => this.setState({ problemDetail: e.target.value })}
              />
              <ErrorMessage name="problemDetail" component="div" className="text-danger" />
            </fieldset>

            <fieldset className="form-group">
              <label>Status</label>
              <Select
                value={this.state.selectedOption}
                options={this.state.options}
                onChange={opt =>
                  this.setState({
                    selectedOption: opt,
                    problemStatus: opt.value
                  })
                }
              />
            </fieldset>

            <button type="submit" className="btn btn-success">
              Save
            </button>

            <button
              type="button"
              className="btn btn-secondary ml-2"
              onClick={this.viewPatient}
            >
              Back
            </button>

          </Form>
        )}
      </Formik>
    );
  }
}
