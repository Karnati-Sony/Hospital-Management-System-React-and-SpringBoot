import React, { Component } from 'react';
import PatientService from '../../services/PatientService';
import alertify from 'alertifyjs';
import "alertifyjs/build/css/alertify.css";
import AlertifyService from '../../services/AlertifyService';

class AddPatientComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            lastname: '',
            email: '',
            phoneNo: '',
            gender: 'Male',
            city: 'CHENNAI',
            bornDate: '',
            status: 1,

            cities: [
                "HYDERABAD", "CHENNAI", "BENGALURU", "MUMBAI", "DELHI",
                "KOLKATA", "PUNE", "VISAKHAPATNAM", "VIJAYAWADA", "WARANGAL",
                "TIRUPATI", "KOCHI", "COIMBATORE", "TRIVANDRUM", "JAIPUR",
                "AHMEDABAD", "SURAT", "BHOPAL", "INDORE", "NAGPUR",
                "PATNA", "LUCKNOW", "BHUBANESWAR", "GUWAHATI", "RAIPUR",
                "RANCHI", "GURGAON", "NOIDA", "CHANDIGARH"
            ]
        };
    }

    // ✅ simple validation
    isInvalid() {
        const { name, lastname, phoneNo, bornDate } = this.state;
        return (
            !name.trim() ||
            !lastname.trim() ||
            !phoneNo.trim() ||
            !bornDate
        );
    }

    saveUser = (e) => {
        e.preventDefault();

        if (this.isInvalid()) {
            AlertifyService.alert("Please fill all required fields (*)");
            return;
        }

        const patient = { ...this.state };

        PatientService.addPatient(patient)
            .then(() => {
                alertify.success("Patient added successfully");
                this.props.history.push('/patients');
            })
            .catch(error => {
                console.error("Error while saving patient:", error);

                if (error.response?.data?.message) {
                    AlertifyService.alert(error.response.data.message);
                } else {
                    AlertifyService.alert("Server error. Please try again.");
                }
            });
    };

    onChangeData = (field, value) => {
        this.setState({ [field]: value });
    };

    back = () => {
        this.props.history.push('/patients');
    };

    render() {
        const { name, lastname, phoneNo, email, bornDate, gender, city, cities } = this.state;

        return (
            <div className="row">
                <div className="col-sm-12">
                    <button className="btn btn-danger" onClick={this.back}>Back</button>
                    <hr />
                </div>

                <div className="col-sm-8">
                    <h2 className="text-center">ADD PATIENT</h2>

                    <form>
                        <div className="form-group">
                            <label>Name *</label>
                            <input
                                type="text"
                                className="form-control"
                                value={name}
                                onChange={e => this.onChangeData('name', e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Last Name *</label>
                            <input
                                type="text"
                                className="form-control"
                                value={lastname}
                                onChange={e => this.onChangeData('lastname', e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Phone *</label>
                            <input
                                type="text"
                                className="form-control"
                                value={phoneNo}
                                onChange={e => this.onChangeData('phoneNo', e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                className="form-control"
                                value={email}
                                onChange={e => this.onChangeData('email', e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Date Of Birth *</label>
                            <input
                                type="date"
                                className="form-control"
                                value={bornDate}
                                max={new Date().toISOString().split("T")[0]}
                                onChange={e => this.onChangeData('bornDate', e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Gender *</label>
                            <select
                                className="form-control"
                                value={gender}
                                onChange={e => this.onChangeData('gender', e.target.value)}
                            >
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label>City *</label>
                            <select
                                className="form-control"
                                value={city}
                                onChange={e => this.onChangeData('city', e.target.value)}
                            >
                                {cities.map(c => (
                                    <option key={c} value={c}>{c}</option>
                                ))}
                            </select>
                        </div>

                        <button
                            className="btn btn-success"
                            onClick={this.saveUser}
                        >
                            Save
                        </button>
                    </form>
                </div>

                <div className="col-lg-3">
                    <img
                        style={{ height: 200 }}
                        src="https://i1.wp.com/www.nosinmiubuntu.com/wp-content/uploads/2013/02/New-Database.png?w=770"
                        alt="patient"
                    />
                </div>
            </div>
        );
    }
}

export default AddPatientComponent;
