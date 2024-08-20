import {Component, Injectable, OnInit} from '@angular/core';
import {Author} from "../model/author.model";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {finalize} from "rxjs";

@Component({
    selector: 'admin-app-authors',
    templateUrl: './authors.component.html',
    styleUrl: './authors.component.css'
})
@Injectable({
    providedIn: 'root'
})
export class AuthorsComponent implements OnInit {
    authors: Author[] | undefined;
    loading: boolean = false;

    constructor(private http: HttpClient) {

    }

    ngOnInit(): void {
        this.loading = true;
        this.http.get<Author[]>(environment.API_URL + environment.AUTHORS_ENDPOINT)
            .pipe(finalize(() => {
                this.loading = false;
            }))
            .subscribe(response => {
                this.authors = response;
            });
    }
}
