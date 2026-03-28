import { IStudentClass } from 'app/shared/model/student-class.model';

export interface IFeeStructure {
  id?: number;
  amount?: number | null;
  academicYear?: string | null;
  studentClass?: IStudentClass | null;
}

export const defaultValue: Readonly<IFeeStructure> = {};
