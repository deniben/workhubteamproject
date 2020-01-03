import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Skill } from '../models/skill.model';
import { URL } from '../../constants';

@Injectable({
  providedIn: "root"
})
export class SkillService {

  skillsURL: string;

  constructor(private client: HttpClient, private router: Router, private builder: FormBuilder) {
    this.skillsURL = URL + '/skills';
  }

  public createSkill(skillName: string) {
    console.log("[create skill]");
    const body = {
      name: skillName
    };
    return this.client.post(this.skillsURL, body);
  }

  public findAll() {
    console.log("[find all skills]")
    return this.client.get<Skill[]>(this.skillsURL);
  }
}
