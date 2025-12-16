import ApiService from "./ApiService";

const PROBLEM_API_BASE_URL = '/problem'; // ✅ ONLY THIS (no /api)
const PROBLEM_STATUS = '/status';
const FIND_ALL = '/find-all-by-patientid/';
const PROBLEM_WITH_PROBLEMID = '/find-by-problemid/';

class ProblemService {

    getProblem(problemid) {
        return ApiService.getAll(PROBLEM_API_BASE_URL + PROBLEM_WITH_PROBLEMID + problemid);
    }

    getAllByPatientId(patientId) {
        return ApiService.getOneById(PROBLEM_API_BASE_URL + FIND_ALL + patientId);
    }

    delete(id) {
        return ApiService.deleteById(PROBLEM_API_BASE_URL + '/' + id);
    }

    add(problem) {
        return ApiService.post(PROBLEM_API_BASE_URL, problem);
    }

    getProblemStatus() {
        return ApiService.getAllDatas(PROBLEM_API_BASE_URL + PROBLEM_STATUS);
    }
}

export default new ProblemService();
