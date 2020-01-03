import { Component, OnInit } from '@angular/core';
import { Skill } from '../models/skill.model';
import { SkillService } from '../services/skill.service'
import { ProjectType } from '../models/projectType.model';
import { ProjectService } from '../services/project.service';

@Component({
  selector: 'app-pt-multiselect',
  templateUrl: './pt-multiselect.component.html',
  styleUrls: ['./pt-multiselect.component.css']
})
export class PrMultiselectComponent implements OnInit {
 

  projectType: ProjectType;
  dropdownList = [];
  selectedTypeItems = [];
  dropdownSettings = {};
  a: Number;
  projectTypeId : Number;

   

  constructor(private projectService: ProjectService) { }

  parseSkillsList(value, index, array) {
    return { 'id': value.id, 'itemName': value.name };
  }

  ngOnInit() {
    this.projectService.AllProjectType().subscribe(data => {
      this.dropdownList = data.map(this.parseSkillsList);
    });


    this.selectedTypeItems = [];
    this.dropdownSettings = {
      singleSelection: true,
      text: "Select ProjectType",
      selectAllText: 'Select All',
      unSelectAllText: 'Remove All',
      enableSearchFilter: true,
      addNewItemOnFilter: true,
      addNewButtonText: 'Add your own projectType',
      classes: "myclass custom-class"
    };
  }

  onAddItem(projectTypeName: string) {
    event.preventDefault();
    console.log(projectTypeName);
    this.projectService.createProjectType(projectTypeName).subscribe(
      data => {
        this.projectType = data as ProjectType;
        this.selectedTypeItems.push({ 'id': this.projectType.id, 'itemName': this.projectType.name });
        this.dropdownList.push({ 'id': this.projectType.id, 'itemName': this.projectType.name })
        console.log(data);
      })
  }

  getSelectedItems(): Number  {

    var data = this.selectedTypeItems.map(t=>t.id);
   
   this.projectTypeId = parseInt(data[0]);
   console.log(data)
   console.log(this.projectTypeId)
    return this.projectTypeId;
  }

}
