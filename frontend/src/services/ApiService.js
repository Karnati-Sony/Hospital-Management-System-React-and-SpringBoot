import axios from "axios";

// ✅ Your Spring Boot backend base URL
const API_BASE_URL = "http://localhost:8185/api";

class ApiService {
    getAll(path) {
        return axios.get(API_BASE_URL + path);
    }

    getOneById(path) {
        return axios.get(API_BASE_URL + path);
    }

    post(path, data) {
        return axios.post(API_BASE_URL + path, data);
    }

    put(path, data) {
        return axios.put(API_BASE_URL + path, data);
    }

    deleteById(path) {
        return axios.delete(API_BASE_URL + path);
    }

    getAllDatas(path) {
        return axios.get(API_BASE_URL + path);
    }
}

export default new ApiService();
