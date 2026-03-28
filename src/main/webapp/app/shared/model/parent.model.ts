export interface IParent {
  id?: number;
  name?: string | null;
  phone?: string | null;
  email?: string | null;
}

export const defaultValue: Readonly<IParent> = {};
