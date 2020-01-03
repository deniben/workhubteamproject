import {Enum, EnumType} from 'ts-jenum';

@Enum('text')
export class PhotoTypes extends EnumType<PhotoTypes>() {

  static readonly PROFILE = new PhotoTypes('PROFILE');
  static readonly PROJECT = new PhotoTypes('PROJECT');
  static readonly COMPANY = new PhotoTypes('COMPANY');

  private constructor(public text: string) {
    super();
  }
}
