package com.github.adminfaces.showcase.bean;

import com.github.adminfaces.showcase.analytics.util.AuthUtil;
import com.github.adminfaces.showcase.model.User;
import java.io.IOException;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by rmpestano on 04/02/17.
 */
@Named
//@ViewScoped
@SessionScoped
//@Getter
//@Setter
//@NoArgsConstructor
public class LogonMB implements Serializable {

    private User user;

    public User getUser() {
        if (this.user == null) {
            this.user = new User();
        }
        return this.user;
    }

    private String email;

    private String password;

    private boolean remember;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    /**
     * BS methods
     */
    public String doLogon() {
        if (this.user != null) {
            Faces.getFlash().setKeepMessages(true);
            //TODO: hashedPassword degeri db den yada ldap tan otomatik alinacak;MD5 en zayif hash algoritmasidir, bunu SHA li hash algoritmalara cek.
            String hashedPassword = AuthUtil.generateMd5Hash(this.user.getPassword());
            if (StringUtils.isNotEmpty(this.user.getUsername()) && this.user.getUsername().equals("admin")
                    && AuthUtil.isUserPasswordValidate(this.user.getPassword(), hashedPassword)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("validated_user", this.user);
                this.user.setIsAuthenticated(true);
                this.user.setFirstname("Arif");
                this.user.setLastname("EROL");
                return "/index.xhtml?faces-redirect=true";
            } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials"));
                Messages.addGlobalError("Kullanici Adi veya Sifre Hatali!");
                return "/login.xhtml?faces-redirect=true";
            }
        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials"));
            Messages.addGlobalError("Kullanici Bilgisi Bulunamadi!");
            return "/login.xhtml?faces-redirect=true";
        }
    }

    public String doLogout() {
        user = new User();
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("valided_user", null);
            HttpServletRequest req = ((HttpServletRequest) ec.getRequest());
            req.logout();
            ec.invalidateSession();
//            ec.redirect("logout");
        } catch (ServletException ex) {
            Logger.getLogger(LogonMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/login.xhtml?faces-redirect=true";
    }

}
