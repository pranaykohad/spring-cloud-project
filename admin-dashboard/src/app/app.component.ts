import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { MessageService } from '../behaviorSubject/message.service';
import { LoaderComponent } from '../loader/loader.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, LoaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit, OnDestroy {
  enableLoader: boolean = false;
  private enableLoaderSubs: Subscription = new Subscription();
  private disableLoaderSubs: Subscription = new Subscription();

  constructor(private messageService: MessageService) {}

  ngOnDestroy(): void {
    this.enableLoaderSubs.unsubscribe();
    this.disableLoaderSubs.unsubscribe();
  }

  ngOnInit(): void {
    this.enableLoaderSubs = this.messageService.currentMessage$.subscribe(
      (msg) => {
        if (msg === 'ENABLE_LOADER') {
          this.enableLoader = true;
        } else if (msg === 'DISABLE_LOADER') {
          this.enableLoader = false;
        }
      }
    );
  }
}
