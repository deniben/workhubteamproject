import { Company } from './company.model';
import { ProjectType } from './projectType.model';
import { Skill } from './skill.model';

export class Project {

    id: BigInteger;

    name: string;

    description: string;

    budget: Number; 

    expiryDate: Date = new Date();

    companyCreator: Company;
    
    photo: string;

    photoUrl: string;

    // numberOfInvite : number;

    employeeMark: BigInteger;

    employerMark: BigInteger;
    
    projectTypeId: Number;

    projectStatus: String = new String;

    projectType: ProjectType[];

    skills:  Skill[];

    skillSet: Skill[];

    status: String;

    flagForRate : number;

}
