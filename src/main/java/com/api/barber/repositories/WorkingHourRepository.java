package com.api.barber.repositories;

import com.api.barber.domain.entities.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, UUID> {
    @Query(value = """
        select wh.*
        from working_hours wh
        where not exists(select 1 from appointments ap where ap.date = :date and ap.status = 'PENDING' and ap.barber_id = :barberId and wh.id = ap.working_hour_id)
        and wh.day_of_week = :dayOfWeek
        order by wh.hour_of_day
        """, nativeQuery = true)
    List<WorkingHour> findAllByDateAndDayOfWeek(@Param("date") LocalDate date,@Param("dayOfWeek")String dayOfWeek,@Param("barberId") UUID barberId);


//    @Query("SELECT ts FROM TimeSlot ts WHERE ts.id NOT IN (SELECT s.timeSlot.id FROM Scheduling s WHERE s.status != 'FINISHED' AND s.date =:date)")
//    List<WorkingHour> findAllByNotScheduledOrFinishedByDate(@Param("date") LocalDate localDate);
}
