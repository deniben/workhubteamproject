import {Enum, EnumType} from 'ts-jenum';

@Enum('text')
export class Actions extends EnumType<Actions>() {

  static readonly CREATE = new Actions('create');
  static readonly UPDATE = new Actions('update');

  private constructor(public text: string) {
    super();
  }
}
