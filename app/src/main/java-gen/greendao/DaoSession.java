package greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Customer;
import greendao.Voucher;

import greendao.CustomerDao;
import greendao.VoucherDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig customerDaoConfig;
    private final DaoConfig voucherDaoConfig;

    private final CustomerDao customerDao;
    private final VoucherDao voucherDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        voucherDaoConfig = daoConfigMap.get(VoucherDao.class).clone();
        voucherDaoConfig.initIdentityScope(type);

        customerDao = new CustomerDao(customerDaoConfig, this);
        voucherDao = new VoucherDao(voucherDaoConfig, this);

        registerDao(Customer.class, customerDao);
        registerDao(Voucher.class, voucherDao);
    }
    
    public void clear() {
        customerDaoConfig.getIdentityScope().clear();
        voucherDaoConfig.getIdentityScope().clear();
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public VoucherDao getVoucherDao() {
        return voucherDao;
    }

}