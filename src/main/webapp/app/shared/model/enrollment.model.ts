import { IStudent } from 'app/shared/model/student.model';
import { ISection } from 'app/shared/model/section.model';

export interface IEnrollment {
  id?: number;
  academicYear?: string;
  rollNumber?: number | null;
  student?: IStudent | null;
  section?: ISection | null;
}

export const defaultValue: Readonly<IEnrollment> = {};
