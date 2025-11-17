import { Component, EventEmitter, Output, Input } from '@angular/core';
import { MatCalendarCellCssClasses } from '@angular/material/datepicker';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent {

  @Output() dataSelecionada = new EventEmitter<Date>();

  @Input() dateClass: (date: Date) => MatCalendarCellCssClasses = () => '';

  public selectedDate: Date | null = null;

  constructor() { }

  onDateSelect(event: Date | null): void {
    if (event) {
      this.selectedDate = event;
      this.dataSelecionada.emit(event);
    }
  }
}
