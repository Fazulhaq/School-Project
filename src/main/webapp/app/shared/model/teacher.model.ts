import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ITeacher {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  hireDate?: dayjs.Dayjs | null;
  qualification?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITeacher> = {};
