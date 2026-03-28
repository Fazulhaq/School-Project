export interface IExam {
  id?: number;
  name?: string;
  academicYear?: string | null;
}

export const defaultValue: Readonly<IExam> = {};
