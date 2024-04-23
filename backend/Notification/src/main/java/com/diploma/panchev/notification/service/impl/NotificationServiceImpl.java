package com.diploma.panchev.notification.service.impl;

import com.diploma.panchev.notification.domain.event.Event;
import com.diploma.panchev.notification.repository.EventRepository;
import com.diploma.panchev.notification.service.NotificationService;
import com.diploma.panchev.notification.service.mapper.NotificationServiceMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final static NotificationServiceMapper MAPPER = Mappers.getMapper(NotificationServiceMapper.class);
    private final EventRepository eventRepository;

    public NotificationServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getNotificationHistory(String groupId, String deviceId, String last, Integer pageSize) {
        if(pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        return eventRepository.getEventHistory(groupId, deviceId, last, pageSize)
                .get()
                .map(MAPPER::map)
                .toList();
    }
}
