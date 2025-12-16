import React from 'react';

const PatientDetail = (props) => {
    return (
        <div className="card">
            <div className="card-header bg-info text-white">
                <h5>Patient Details</h5>
            </div>

            <div className="card-body">
                <p><b>ID:</b> {props.patientid || '-'}</p>
                <p><b>Name:</b> {props.name || '-'}</p>
                <p><b>Last Name:</b> {props.lastname || '-'}</p>
                <p><b>Email:</b> {props.email || '-'}</p>
                <p><b>Phone:</b> {props.phoneNo || '-'}</p>
                <p><b>Gender:</b> {props.gender || '-'}</p>
                <p><b>City:</b> {props.city || '-'}</p>
                <p><b>Date of Birth:</b>{" "}
                    {props.bornDate
                        ? props.bornDate.substring(0, 10)
                        : '-'}
                </p>
            </div>
        </div>
    );
};

export default PatientDetail;
