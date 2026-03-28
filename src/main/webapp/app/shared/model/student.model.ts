import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { StudentStatus } from 'app/shared/model/enumerations/student-status.model';

export interface IStudent {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  admissionDate?: dayjs.Dayjs | null;
  status?: keyof typeof StudentStatus | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IStudent> = {};
