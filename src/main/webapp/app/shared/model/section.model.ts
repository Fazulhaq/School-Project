import { IStudentClass } from 'app/shared/model/student-class.model';

export interface ISection {
  id?: number;
  name?: string;
  studentClass?: IStudentClass | null;
}

export const defaultValue: Readonly<ISection> = {};
