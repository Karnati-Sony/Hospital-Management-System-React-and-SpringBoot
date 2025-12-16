import React, { Component } from 'react'
import PatientService from '../../services/PatientService';
import "@material/react-checkbox/dist/checkbox.css";
import Checkbox from '@material/react-checkbox';
import "alertifyjs/build/css/themes/default.min.css";
import "alertifyjs/build/css/themes/bootstrap.min.css";
import "alertifyjs/build/css/alertify.min.css";
import "../../Assets/css/ListPatientComponent.css"
// import Modal from 'react-modal'; 
import * as alertify from 'alertifyjs';

import Moment from 'react-moment';
import PatientDetailModal from '../BasicComponent/PatientDetailModal';

const items = [
    'Name',
    'Lastname',
    'Email',
    'City'
];

let filterArray = [];
let checked = {
    name: false,
    lastname: false,
    email: false,
    city: false
};
// ✅ make this an empty array instead of undefined
let filterAllPatients = [];

class ListPatientComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            patients: [],
            message: null,
            indeterminate: false,
            filters: [],
            patient: {}
        }
        this.reloadPatientList = this.reloadPatientList.bind(this);
    }

    componentDidMount() {
        this.reloadPatientList();
    }

    reloadPatientList() {
        PatientService.getPatients().then((res) => {
            const data = res.data || [];
            this.setState({ patients: data });
            filterAllPatients = data;          // ✅ always an array
        }).catch(err => {
            console.error("Error loading patients", err);
            this.setState({ patients: [] });
            filterAllPatients = [];
        });
    }

    deletePatient(patientid) {
        alertify.confirm(
            "Are you sure to delete this patient.",
            ok => {
                PatientService.deletePatient(patientid).then(res => {
                    this.setState({
                        message: 'User deleted successfully. ' + res,
                        patients: this.state.patients.filter(patient => patient.patientid !== patientid)
                    });
                });
                alertify.success('To delete patient is ok');
            },
            cancel => { alertify.error('Cancel'); }
        ).set({ title: "Attention" }).set({ transition: 'slide' }).show();
    }

    editPatient(id) {
        alertify.confirm(
            "Are you sure to edit this patient.",
            ok => {
                window.localStorage.setItem("patientId", id);
                this.props.history.push('/edit-patient');
            },
            cancel => { alertify.error('Cancel'); }
        ).set({ title: "Attention" }).set({ transition: 'slide' }).show();
    }

    viewPatient(patientid) {
        window.localStorage.setItem("patientId", patientid);
        this.props.history.push('/view-patient/' + patientid);
    }

    addPatient() {
        this.props.history.push('/add-patient');
    }

    onChangeSearchByName = (e) => {
        this.filterPatients(e.target.value || '');
    }

    filterPatients = (value) => {
        if (filterArray.length > 0) {
            // ✅ if patients not loaded yet, just do nothing
            const source = Array.isArray(filterAllPatients) ? filterAllPatients : [];

            if (value !== '' && value.length > 0) {
                const lowerValue = value.toLowerCase();
                const results = source.filter(patient => {
                    let found = false;
                    filterArray.forEach(function (filter) {
                        const key = filter.toLowerCase(); // name, lastname, email, city
                        const fieldValue = (patient[key] || '').toString().toLowerCase();
                        if (fieldValue.indexOf(lowerValue) > -1) {
                            found = true;
                        }
                    });
                    return found;
                });
                this.setState({ patients: results });
            } else {
                this.reloadPatientList();
            }
        } else {
            alertify.set('notifier', 'delay', 2);
            alertify.error('Please select any parameters');
        }
    }

    createCheckboxes = () => (items.map((item) => this.createCheckbox(item)))

    createCheckbox = label => (
        <div className="float-left" style={{ margin: "0 25px 0 0" }} key={label} >
            <Checkbox
                nativeControlId='my-checkbox'
                checked={checked[label.toLowerCase()]}
                onChange={(e) => { this.changeStateForChecked(e, label); }}
            />
            <label className="checkbox-label" ><b>{label}</b></label>
        </div>
    )

    changeStateForChecked = (e, label) => {
        const key = label.toLowerCase(); // 'Name' -> 'name'
        checked[key] = e.target.checked;
        const index = filterArray.indexOf(label);
        if (checked[key]) {
            if (index === -1) { filterArray.push(label); }
        } else {
            if (index !== -1) { filterArray.splice(index, 1); }
        }
    }

    viewPatientQuickly(patient) {
        this.setState({ patient });
    }

    render() {
        return (
            <div className="row">
                <div className="col-lg-12">
                    <button
                        className="btn btn-warning"
                        onClick={() => this.addPatient()}>
                        Add Patient
                    </button>
                    <hr />
                </div>

                <div className="col-lg-6" >
                    <div className="form-group">
                        <input
                            type="text"
                            placeholder="Search Patient by choosing any parameter"
                            name="searchByName"
                            className="form-control"
                            onChange={this.onChangeSearchByName}
                        />
                    </div>
                    <hr />
                </div>

                <div className="col-lg-6">
                    {this.createCheckboxes()}
                </div>

                <div className="col-lg-12">
                    <div className="table-responsive-lg">
                        <table className="table table-bordered table-sm table-dark table-hover" style={{ textAlign: "center" }}>
                            <thead>
                                <tr>
                                    <th>Full Name</th>
                                    <th>Email</th>
                                    <th>Date Of Birth</th>
                                    <th>City</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody >
                                {this.state.patients.map(patient =>
                                    <tr className={patient.gender === "Male" ? "bg-default" : "bg-danger"} key={patient.patientid}>
                                        <td>{patient.name} {patient.lastname}</td>
                                        <td>{patient.email}</td>
                                        <td>
                                            {patient.bornDate !== null ?
                                                <Moment format="YYYY/MM/DD HH:mm">
                                                    {patient.bornDate}
                                                </Moment>
                                                : null}
                                        </td>
                                        <td>{patient.city}</td>
                                        <td>
                                            <div className="btn-group" role="group">
                                                <button id="btnGroupDrop1"
                                                    type="button"
                                                    className="btn btn-secondary btn-sm dropdown-toggle"
                                                    data-toggle="dropdown"
                                                    aria-haspopup="true"
                                                    aria-expanded="false"> Actions </button>
                                                <div className="dropdown-menu" aria-labelledby="btnGroupDrop1">
                                                    <button
                                                        className="dropdown-item"
                                                        onClick={() => this.viewPatient(patient.patientid)} >  View </button>
                                                    <div className="dropdown-divider"></div>
                                                    <button
                                                        className="dropdown-item"
                                                        data-toggle="modal" data-target="#patientModal"
                                                        onClick={() => this.viewPatientQuickly(patient)} >  View Quickly </button>
                                                    <div className="dropdown-divider"></div>
                                                    <button
                                                        className="dropdown-item"
                                                        onClick={() => this.editPatient(patient.patientid)} > Edit</button>
                                                    <div className="dropdown-divider"></div>
                                                    <button
                                                        className="dropdown-item"
                                                        onClick={() => this.deletePatient(patient.patientid)}> Delete </button>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>

                        <PatientDetailModal patient={this.state.patient} />
                        <hr /><hr /><hr /><hr />
                    </div>
                </div>
            </div>
        );
    }
}

export default ListPatientComponent;
