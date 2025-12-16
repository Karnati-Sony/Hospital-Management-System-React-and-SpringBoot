import React, { Component } from 'react';
import PatientService from '../../services/PatientService';
import PatientDetail from './PatientDetail';
import AlertifyService from '../../services/AlertifyService';
import ProblemsComponent from './ProblemComponent/ProblemsComponent';

export default class ViewPatientComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            patientid: props.match.params.patientid,
            patient: null,
            problems: []
        };
        this.loadPatient = this.loadPatient.bind(this);
    }

    componentDidMount() {
        this.loadPatient();
    }

    loadPatient() {
        PatientService.getPatientById(this.state.patientid)
            .then(res => {
                const p = res.data;
                this.setState({
                    patient: p,
                    problems: p?.problems || []
                });
            })
            .catch(error => {
                if (error.response) {
                    AlertifyService.alert(error.response.data.message);
                    this.props.history.push('/patients');
                } else {
                    console.error(error);
                }
            });
    }

    viewProblemForm(patientid) {
        window.localStorage.setItem("patientId", patientid);
        this.props.history.push('/add-problem');
    }

    back() {
        this.props.history.push('/patients');
    }

    render() {
        const { patient } = this.state;

        // ✅ IMPORTANT: avoid first-render crash
        if (!patient) {
            return <div className="text-center mt-5">Loading patient details...</div>;
        }

        return (
            <div className="row">

                <div className="col-lg-12 mb-2">
                    <button
                        className="btn btn-danger"
                        onClick={() => this.back()}>
                        Back
                    </button>

                    <button
                        className="btn btn-warning ml-2"
                        onClick={() => this.viewProblemForm(patient.patientid)}>
                        Add Problem
                    </button>
                    <hr />
                </div>

                <div className="col-lg-7">
                    <PatientDetail
                        patientid={patient.patientid}
                        name={patient.name}
                        lastname={patient.lastname}
                        phoneNo={patient.phoneNo}
                        email={patient.email}
                        city={patient.city}
                        bornDate={patient.bornDate}
                        gender={patient.gender}
                        showButtons={true}
                    />
                </div>

                <div className="col-lg-4">
                    <img
                        style={{ height: 300 }}
                        src="https://cdn4.iconfinder.com/data/icons/business-colored-vol-1/100/business-colored-7-05-512.png"
                        alt="patient"
                    />
                </div>

                <div className="col-lg-12 mt-3">
                    <ProblemsComponent patientid={patient.patientid} />
                </div>

            </div>
        );
    }
}
