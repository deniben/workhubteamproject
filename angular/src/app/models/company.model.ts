import { Skill } from './skill.model';

export class Company {

    id: BigInteger;

    name: string;

    description: string;

    avgMark: number;

    type: string;

    skills:  Skill[];

    photoUrl: string

    blocked: boolean;

   isOwner: boolean;

}
