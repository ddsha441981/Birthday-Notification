package com.cwc.birthday.notification.service;

import com.cwc.birthday.notification.model.Birthday;

import java.util.function.Supplier;

public interface NotificationServiceAlert {
     <T> T notifyNotification(Supplier<T> supplier, Birthday birthday);
}
