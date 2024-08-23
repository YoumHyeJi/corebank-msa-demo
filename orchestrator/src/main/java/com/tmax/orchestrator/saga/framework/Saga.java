package com.tmax.orchestrator.saga.framework;

import com.tmax.orchestrator.domain.EventLog;
import com.tmax.orchestrator.event.EventLogService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * 각각의 API 마다 해당 Saga 클래스를 상속받는 xxxSaga 클래스를 생성해야 한다.
 * Saga 클래스에서는 현재 처리중인 Event에 대한 EventLog를 저장하는 ensureProcessed() 메서드가 구현되어 있다.
 */
@RequiredArgsConstructor
public abstract class Saga {

    private final EventLogService eventLogService;

    /**
     * EventLog 테이블에 현재 처리하고 있는 Event가 이미 존재하는지 확인한다.
     * 1) 이미 존재하는 경우, 그냥 리턴한다.
     * 2) 존재하지 않는 경우, callback 메서드를 실행 한 뒤 해당 Event에 대한 EventLog를 저장한다.
     *
     * @param eventId : 현재 처리 중인 eventId
     * @param callback : EventLog에 현재 처리중인 Event가 존재하지 않는 경우, 실행하고자 하는 callback 메서드
     */
    protected void ensureProcessed(UUID eventId, Runnable callback){
        // case 1. 이미 처리된 이벤트인 경우
        if(eventLogService.findEventLog(eventId) != null){
            return;
        }

        // case 2. 아직 처리되지 않은 이벤트인 경우
        callback.run();
        eventLogService.createEventLog(new EventLog(eventId));
    }

}
