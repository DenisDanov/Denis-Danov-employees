import {PairDataDTO} from "./PairDataDTO.ts";
import {EmployeeDTO} from "./EmployeeDTO.ts";

export interface EmployeePairDTO {
    employee1: EmployeeDTO;
    employee2: EmployeeDTO;
    pairDataDTO: PairDataDTO;
}