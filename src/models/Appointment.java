package models;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {

    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty descriptionProperty = new SimpleStringProperty();
    private Property<Room> roomProperty =  new ObjectPropertyBase<Room>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "room";
        }
    };

    private Property<LocalDate> dateProperty = new ObjectPropertyBase<LocalDate>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "date";
        }
    };

    private Property<LocalTime> startTimeProperty = new ObjectPropertyBase<LocalTime>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "start time";
        }
    };
    private Property<LocalTime> endTimeProperty = new ObjectPropertyBase<LocalTime>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "end time";
        }
    };

    public String getTitleProperty() {
        return titleProperty.get();
    }

    public void setTitleProperty(String titleProperty) {
        this.titleProperty.set(titleProperty);
    }

    public StringProperty titlePropertyProperty() {
        return titleProperty;
    }

    public String getDescription() {
        return descriptionProperty.getValue();
    }

    public void setDescription(String formal) {
        descriptionProperty.setValue(formal);
    }

    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }


    public Room getRoom() {
        return roomProperty.getValue();
    }

    public void setRom(Room room) {
        roomProperty.setValue(room);
    }

    public Property<Room> romProperty() {
        return roomProperty;
    }

    public LocalDate getDate() {
        return dateProperty.getValue();
    }

    public void setDate(LocalDate date) {
        dateProperty.setValue(date);
    }

    public Property<LocalDate> DateProperty() {
        return dateProperty;
    }

    public LocalTime getStartTime() {
        return startTimeProperty.getValue();
    }

    public void setStartTime(LocalTime startTime) {
        startTimeProperty.setValue(startTime);
    }

    public Property<LocalTime> startTimeProperty() {
        return startTimeProperty;
    }

    public LocalTime getEndTime() {
        return endTimeProperty.getValue();
    }

    public void setEndTime(LocalTime endTime) {
        endTimeProperty.setValue(endTime);
    }

    public Property<LocalTime> getEndTimeProperty() {
        return endTimeProperty;
    }



}

