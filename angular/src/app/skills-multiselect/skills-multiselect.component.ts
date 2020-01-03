import { Component, OnInit, ViewChild } from '@angular/core';
import { Skill } from '../models/skill.model';
import { SkillService } from '../services/skill.service'
import { AngularMultiSelect } from 'angular2-multiselect-dropdown';


@Component({
  selector: 'app-skills-multiselect',
  templateUrl: './skills-multiselect.component.html',
  styleUrls: ['./skills-multiselect.component.css']
})
export class SkillsMultiselectComponent implements OnInit {

  skill: Skill;
  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};

  @ViewChild(AngularMultiSelect, { static: false }) 
  varietalDropdown: AngularMultiSelect;

  constructor(private skillService: SkillService) { }

  parseSkillsListToUI(value, index, array) {
    return { 'id': value.id, 'itemName': value.name };
  }

  parseSkillsListToJSON(value) {
    return new Skill(value.id, value.itemName);
  }

  ngOnInit() {
    this.skillService.findAll().subscribe(data => {
      this.dropdownList = data.map(this.parseSkillsListToUI);
    });

    this.selectedItems = [];

    this.dropdownSettings = {
      singleSelection: false,
      text: 'Select Skills',
      selectAllText: 'Select All',
      unSelectAllText: 'Remove All',
      enableSearchFilter: true,
      addNewItemOnFilter: true
      // addNewButtonText: 'Add your own skill',
    };
  }

  onAddItem(skillName: string) {
    event.preventDefault();
    console.log(skillName);
    this.skillService.createSkill(skillName).subscribe(
      data => {
        this.skill = data as Skill;
        this.selectedItems.push({ 'id': this.skill.id, 'itemName': this.skill.name });
        this.dropdownList.push({ 'id': this.skill.id, 'itemName': this.skill.name })
        console.log(data);
        this.varietalDropdown.closeDropdown();
      })
  }

  getSelectedItems(): Skill[] {
    console.log(this.selectedItems);
    return this.selectedItems.map(this.parseSkillsListToJSON);
  }

}
