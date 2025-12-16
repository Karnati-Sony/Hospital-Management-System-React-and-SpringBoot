import axios from "axios";

const BASE_URL = "http://localhost:8185/api/patient";

class PatientService {

    getPatients() {
        return axios.get(BASE_URL);
    }

    
    getPatientById(patientId) {
        return axios.get(`${BASE_URL}/find-by-id/${patientId}`);
    }

    deletePatient(id) {
        return axios.delete(`${BASE_URL}/${id}`);
    }

  
    addPatient(patient) {
        return axios.post(BASE_URL, patient);
    }

  
    editPatient(patient) {
        return axios.put(`${BASE_URL}/${patient.patientid}`, patient);
    }

  
    getCities() {
        return axios.get(`${BASE_URL}/cities`);
    }
}

const patientService = new PatientService();
export default patientService;
