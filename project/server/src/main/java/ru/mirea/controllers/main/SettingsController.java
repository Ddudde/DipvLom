package ru.mirea.controllers.main;

import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.Main;
import ru.mirea.controllers.AuthController;
import ru.mirea.data.SSE.Subscriber;
import ru.mirea.data.SSE.TypesConnect;
import ru.mirea.data.models.auth.SettingUser;
import ru.mirea.data.models.auth.User;
import ru.mirea.services.ServerService;

import java.util.Objects;

@RequestMapping("/settings")
@NoArgsConstructor
@RestController public class SettingsController {

    @Autowired
    private ServerService datas;

    @Autowired
    private AuthController authController;

    @PostMapping(value = "/chBool")
    public JsonObject chBool(@RequestBody DataSettings body) {
        Subscriber subscriber = authController.getSubscriber(body.uuid);
        User user = datas.userByLogin(subscriber.getLogin());
        try {
            body.wrtr = datas.ini(body.toString());
            if(user != null) {
                SettingUser settingUser = user.getSettings();
                switch (body.id) {
                    case "checkbox_hints" -> settingUser.setHints(body.val);
                    case "checkbox_notify" -> settingUser.setNotif(body.val);
                    case "checkbox_notify_sched" -> settingUser.setNChangeShedule(body.val);
                    case "checkbox_notify_marks" -> settingUser.setNNewMarks(body.val);
                    case "checkbox_notify_yo" -> settingUser.setNNewNewsYO(body.val);
                    case "checkbox_notify_por" -> settingUser.setNNewNewsPor(body.val);
                    case "checkbox_notify_new_sch" -> settingUser.setNNewReqSch(body.val);
                    default -> {}
                }
                datas.getSettingUserRepository().saveAndFlush(settingUser);
                body.wrtr.name("yes").value(true);
            }
        } catch (Exception e) {body.bol = Main.excp(e);}
        return datas.getObj(ans -> {}, body.wrtr, body.bol);
    }

    @PostMapping(value = "/chPass")
    public JsonObject chPass(@RequestBody DataSettings body) {
        Subscriber subscriber = authController.getSubscriber(body.uuid);
        User user = datas.userByLogin(subscriber.getLogin());
        try {
            body.wrtr = datas.ini(body.toString());
            if(user != null) {
                SettingUser settingUser = user.getSettings();
                if(!Objects.equals(settingUser.getSecFr(), body.secFR)){
                    body.wrtr.name("error").value(3);
                } else if(!Objects.equals(user.getPassword(), body.oPar)){
                    body.wrtr.name("error").value(2);
                } else {
                    user.setPassword(body.nPar);
                    datas.getUserRepository().saveAndFlush(user);
                }
            }
        } catch (Exception e) {body.bol = Main.excp(e);}
        return datas.getObj(ans -> {}, body.wrtr, body.bol);
    }

    @PostMapping(value = "/chSecFR")
    public JsonObject chSecFR(@RequestBody DataSettings body) {
        Subscriber subscriber = authController.getSubscriber(body.uuid);
        User user = datas.userByLogin(subscriber.getLogin());
        try {
            body.wrtr = datas.ini(body.toString());
            if(user != null) {
                SettingUser settingUser = user.getSettings();
                settingUser.setSecFr(body.secFR);
                datas.getSettingUserRepository().saveAndFlush(settingUser);
                body.wrtr.name("yes").value(true);
            }
        } catch (Exception e) {body.bol = Main.excp(e);}
        return datas.getObj(ans -> {}, body.wrtr, body.bol);
    }

    @PostMapping(value = "/chIco")
    public JsonObject chIco(@RequestBody DataSettings body) {
        Subscriber subscriber = authController.getSubscriber(body.uuid);
        User user = datas.userByLogin(subscriber.getLogin());
        try {
            body.wrtr = datas.ini(body.toString());
            if(user != null) {
                SettingUser settingUser = user.getSettings();
                settingUser.setIco(body.ico);
                datas.getSettingUserRepository().saveAndFlush(settingUser);
                body.wrtr.name("yes").value(true);
            }
        } catch (Exception e) {body.bol = Main.excp(e);}
        return datas.getObj(ans -> {}, body.wrtr, body.bol);
    }

    @PostMapping(value = "/getSettings")
    public JsonObject getSettings(@RequestBody DataSettings body) {
        Subscriber subscriber = authController.getSubscriber(body.uuid);
        User user = datas.userByLogin(subscriber.getLogin());
        try {
            body.wrtr = datas.ini(body.toString());
            if(user != null) {
                SettingUser settingUser = user.getSettings();
                body.wrtr.name("body").beginObject()
                    .name("checkbox_hints").value(settingUser.getHints())
                    .name("checkbox_notify").value(settingUser.getNotif())
                    .name("checkbox_notify_sched").value(settingUser.getNChangeShedule())
                    .name("checkbox_notify_marks").value(settingUser.getNNewMarks())
                    .name("checkbox_notify_yo").value(settingUser.getNNewNewsYO())
                    .name("checkbox_notify_por").value(settingUser.getNNewNewsPor())
                    .name("checkbox_notify_new_sch").value(settingUser.getNNewReqSch())
                    .endObject();
            }
        } catch (Exception e) {body.bol = Main.excp(e);}
        return datas.getObj(ans -> {}, body.wrtr, body.bol);
    }

    @PostMapping(value = "/setInfo")
    public JsonObject setInfo(@RequestBody DataSettings body) {
        try {
            body.wrtr = datas.ini(body.toString());
        } catch (Exception e) {body.bol = Main.excp(e);}
        return datas.getObj(ans -> {
            authController.infCon(body.uuid, null, TypesConnect.SETTINGS, "main", "main", "main", "main");
        }, body.wrtr, body.bol);
    }
}

@ToString
@NoArgsConstructor @AllArgsConstructor
class DataSettings {
    public String uuid, secFR, oPar, nPar, id;
    public int ico;
    public boolean val;
    public transient boolean bol = true;
    public transient JsonTreeWriter wrtr;
}