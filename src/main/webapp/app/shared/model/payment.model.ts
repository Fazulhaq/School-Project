import dayjs from 'dayjs';
import { IStudent } from 'app/shared/model/student.model';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';

export interface IPayment {
  id?: number;
  amount?: number | null;
  paymentDate?: dayjs.Dayjs | null;
  method?: keyof typeof PaymentMethod | null;
  student?: IStudent | null;
}

export const defaultValue: Readonly<IPayment> = {};
