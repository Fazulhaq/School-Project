import dayjs from 'dayjs';
import { ISection } from 'app/shared/model/section.model';
import { ISubject } from 'app/shared/model/subject.model';
import { ITeacher } from 'app/shared/model/teacher.model';

export interface ITimetable {
  id?: number;
  dayOfWeek?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  section?: ISection | null;
  subject?: ISubject | null;
  teacher?: ITeacher | null;
}

export const defaultValue: Readonly<ITimetable> = {};
