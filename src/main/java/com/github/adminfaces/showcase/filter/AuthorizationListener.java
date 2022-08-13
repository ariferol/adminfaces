/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.showcase.filter;

import com.github.adminfaces.showcase.model.User;
import javax.enterprise.inject.Default;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author aerol
 */
/**
 * User in yetkili olup olmadigini custom PhaseListener araciligi ile kontrol
 * edebiliriz. Yetkili degilse ckis yaptiriyoruz.
 */
@Named("authorizationListener")
@Default
public class AuthorizationListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();
        if (!isAuthorized(fc)) {
            if (fc.getViewRoot().getViewId() != null && !fc.getViewRoot().getViewId().contains("login")) {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                if (fc.getViewRoot().getViewId().contains("logout")) {
//                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Çıkış Yapıldı."));
                } else {
//                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Yetkisiz Erişim "));
                }
                NavigationHandler handler = fc.getApplication().getNavigationHandler();
                handler.handleNavigation(fc, null, "logout");
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    User getUser(FacesContext fc) {
        User user = fc.getApplication().evaluateExpressionGet(fc, "#{logonMB.user}", User.class);
        User user2 = (User) fc.getExternalContext().getSessionMap().get("validated_user");
        return user;
    }

    /**
     * Burada user authentication ve authorization business logic i olmalidir
     */
    boolean isAuthorized(FacesContext fc) {
        boolean isAuthenticatedAndAuthorized = false;
        if (fc.getViewRoot().getViewId() != null && fc.getViewRoot().getViewId().equals("/login.xhtml")) {
            isAuthenticatedAndAuthorized = true;
        } else {
            User user = getUser(fc);
            isAuthenticatedAndAuthorized = user != null
                    && StringUtils.isNotEmpty(user.getUsername())
                    && user.isIsAuthenticated();
        }
        return isAuthenticatedAndAuthorized;
    }

}
