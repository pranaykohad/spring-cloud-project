import { Component, Input } from '@angular/core';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './loader.component.html',
})
export class LoaderComponent {
  @Input()
  enableLoader!: boolean;
}
