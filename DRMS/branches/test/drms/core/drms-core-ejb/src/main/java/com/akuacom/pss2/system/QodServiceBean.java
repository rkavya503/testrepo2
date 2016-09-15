package com.akuacom.pss2.system;

import static com.akuacom.utils.config.RuntimeSwitches.ENABLE_QODS_PROP;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.jboss.logging.Logger;
import org.jboss.wsf.spi.annotation.WebContext;

import com.akuacom.utils.lang.FileUtil;

@Stateless
@WebService
@WebContext(authMethod="BASIC")
public class QodServiceBean extends QuotationGenEAOBean implements
        QodService.L, QodService.R {
    Random rdm = new Random(System.currentTimeMillis());
    Logger log = Logger.getLogger(QodServiceBean.class);
    static List<Quotation> Qods = Collections.<Quotation> emptyList();
    static List<String> QodIds = Collections.<String> emptyList();
    private static boolean USE_IDS = true;
    private static boolean HAVE_DB = true;
    
    public QodServiceBean() {
        super(Quotation.class);
    }

    private boolean enabledQ() {
        return "true".equals(System.getProperty(ENABLE_QODS_PROP));
    }

    private List<String> getAllIds() {
        return em.createNamedQuery("Quotation.getAllIds").getResultList();
    }

    @Override
    public synchronized void initialize() {
        try {
            HAVE_DB = available();
            if(USE_IDS && HAVE_DB) {
                if (QodIds.isEmpty()) {
                    log.info("QodIds empty");
                    File f = new File(Quotation.SER_IDS);
                    if (!f.exists()) {
                        log.info("no ./" + Quotation.SER_IDS);
                        QodIds = getAllIds();
                        log.info("fetched " + QodIds.size() + " ids");
                        FileUtil.toFile(QodIds,Quotation.SER_IDS);
                        log.info("unmarshalled " + QodIds.size() + " ids");
                        if (!USE_IDS) {
                            log.info("clearing ids after serialization");
                            QodIds.clear();
                        }
                    } else {
                        log.info("./" + Quotation.SER_IDS + " exists, marshalling");
                        QodIds =  FileUtil.<List>fromFile(Quotation.SER_IDS, List.class);
                        log.info("marshalled " + QodIds.size() + " qodIds");
                    }
                }
            } else {
                if (Qods.isEmpty()) {
                    log.info("Qods empty");
                    File f = new File(Quotation.SER_BIN);
                    if (!f.exists() && HAVE_DB) {
                        log.info("no ./" + Quotation.SER_BIN);
                        Qods = findAll();
                        log.info("hydrated " + Qods.size() + " qods");
                        FileUtil.toFile(Qods,Quotation.SER_BIN);
                        log.info("unmarshalled " + Qods.size() + " qods");
                    } else {
                        log.info("./" + Quotation.SER_BIN + " exists, marshalling");
                        Qods =  FileUtil.<List>fromFile(Quotation.SER_BIN, List.class);
                        log.info("marshalled " + Qods.size() + " qods");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }


    public Quotation nextQod() {
        if (!enabledQ() ) {
            log.info("disabled");
            return Quotation.NO_QUOTE;
        }
        if (USE_IDS && HAVE_DB) {
            log.info("useids!");
            if (QodIds.isEmpty()) {
                log.info("empty ids!");
                new Thread() {
                    public void run() {
                        log.info("init thread");
                        QodServiceBean.this.initialize();
                    }
                }.start();
                return Quotation.NO_QUOTE;
            } else {
                return em.find(Quotation.class,
                        QodIds.get(rdm.nextInt(QodIds.size())));
            }
        } else {
            if (Qods.isEmpty()) {
                log.info("empty!");
                new Thread() {
                    public void run() {
                        log.info("init thread");
                        QodServiceBean.this.initialize();
                    }
                }.start();
                return Quotation.NO_QUOTE;
            }
            return Qods.get(rdm.nextInt(Qods.size()));
        }
    }

    @WebMethod
    public String nextQodHtml() {
        return nextQod().toHtml();
    }

}
