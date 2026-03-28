import { ITeacher } from 'app/shared/model/teacher.model';
import { ISubject } from 'app/shared/model/subject.model';
import { ISection } from 'app/shared/model/section.model';

export interface ITeacherSubject {
  id?: number;
  teacher?: ITeacher | null;
  subject?: ISubject | null;
  section?: ISection | null;
}

export const defaultValue: Readonly<ITeacherSubject> = {};
