import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';

@Component({
  selector: "app-search",
  templateUrl: "./search.component.html",
  styleUrls: ["./search.component.css"]
})
export class SearchComponent implements OnInit {
  projects;

  proposal;

  types;
  cache;
  submitted;

  pageNumber;
  pagesCount;

  page_size;

  invalidBudget;
  positiveError;

  searchForm;

  constructor(
    private path: ActivatedRoute,
    private client: HttpClient,
    private builder: FormBuilder
  ) {}

  ngOnInit() {
    this.submitted = false;
    this.positiveError = false;
    this.pageNumber = 0;
    this.pagesCount = 0;
    this.page_size = 9;

    this.client.get(environment.api_url + '/project/types').subscribe((data) => {

      this.types = data;

    });

    this.projects = [];

    this.searchForm = this.builder.group({
      name: [],
      typeId: [],
      minBudget: [Validators.min(0.0), Validators.max(1000000.0)],
      maxBudget: [Validators.min(0.0), Validators.max(1000000.0)]
    });

    this.path.paramMap.subscribe(params => {
      if (params.get("name")) {
        this.searchForm.patchValue({ name: params.get("name") });
        this.doSearch({ name: params.get("name") });
      } else {
        this.emptySearch();
      }
    });
  }

  search(data) {
    this.submitted = true;

    if (data.minBudget && data.maxBudget && data.minBudget > data.maxBudget) {
      this.invalidBudget = true;
      return;
    }

    if (
      (data.minBudget && data.maxBudget && data.minBudget < 0) ||
      data.maxBudget < 0
    ) {
      this.positiveError = true;
      return;
    }

    this.invalidBudget = false;
    this.positiveError = false;

    if (this.searchForm.invalid) {
      return;
    }

    this.doSearch({
      name: data.name,
      typeId: data.typeId,
      minBudget: data.minBudget,
      maxBudget: data.maxBudget
    });
  }

  change_page(number) {
    this.doSearch({
      name : this.searchForm.value.typeId,
      typeId : this.searchForm.value.typeId,
      minBudget : this.searchForm.value.minBudget,
      maxBudget : this.searchForm.value.maxBudget,
      page : number
    });
  }

  parse(data) {
    this.projects = [];

    if (data.proposalName) {
      this.proposal = data.proposalName;

      this.cache = {
        typeId: this.searchForm.value.typeId,
        minBudget: this.searchForm.value.minBudget,
        maxBudget: this.searchForm.value.maxBudget
      };
    } else {
      this.proposal = null;
    }

    for (let project of data.result) {
      this.projects.push(project);
    }

    if (data.pagesCount) {
      this.pagesCount = data.pagesCount;
    }
  }

  propose(proposal) {
    this.searchForm.patchValue({ name: proposal });

    this.doSearch({
      name: proposal,
      typeId: this.searchForm.value.typeId,
      minBudget: this.searchForm.value.minBudget,
      maxBudget: this.searchForm.value.maxBudget
    });
  }

  doSearch(params) {
    this.client.post(environment.api_url + '/search', params).subscribe((data) => {
        this.parse(data);
      });
  }

  emptySearch() {
    this.client.post(environment.api_url + '/search', {}).subscribe((data) => {
          this.parse(data);
    });
  }
}
