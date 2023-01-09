import { Component, isDevMode, OnInit } from '@angular/core';
import { MovieService } from 'src/app/services/movie.service';
import { environment } from 'src/environment/environment';

@Component({
  selector: 'app-root',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class AppComponent implements OnInit {
  title = 'MovieParserUI';

  constructor(private movieService: MovieService) {}

  movies: any = []

  ngOnInit() {
    if (isDevMode()) {
      console.log('Development!');
    } else {
      console.log('Production!');
    }

    this.movieService.getAllMovies().subscribe((movies) => {
      this.movies = movies
      console.log(this.movies)
    })
    
  }
}
