import dayjs from 'dayjs';
import { IStudent } from 'app/shared/model/student.model';
import { AttendanceStatus } from 'app/shared/model/enumerations/attendance-status.model';

export interface IAttendance {
  id?: number;
  date?: dayjs.Dayjs;
  status?: keyof typeof AttendanceStatus | null;
  student?: IStudent | null;
}

export const defaultValue: Readonly<IAttendance> = {};
