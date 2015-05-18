package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator
{

    public static Schema schema;

    public static void main(String args[]) throws Exception
    {
        schema = new Schema(3, "greendao");

        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("customerName");
        customer.addStringProperty("customerEmail");
        customer.addStringProperty("customerPhone");
        customer.addDoubleProperty("customerPoint");


        Entity voucher = schema.addEntity("Voucher");
        Property voucherID = voucher.addIdProperty().autoincrement().getProperty();

        voucher.addStringProperty("voucherCode");
        voucher.addDoubleProperty("voucherAmount");
        voucher.addStringProperty("voucherGeneratedTime");
        voucher.addBooleanProperty("isApplied");
        voucher.addStringProperty("appliedTime");

        voucher.addLongProperty("appliedOutletID");
        voucher.addStringProperty("appliedOutletName");

        voucher.addLongProperty("appliedCompanyID");
        voucher.addStringProperty("appliedCompanyName");

        voucher.addLongProperty("appliedUserID");
        voucher.addStringProperty("appliedUserName");

        voucher.addStringProperty("appliedBillCode");

        Property customerID = voucher.addLongProperty("customerID").getProperty();
        ToMany cusToVoucher = customer.addToMany(voucher, customerID);
        cusToVoucher.orderDesc(voucherID);


        new DaoGenerator().generateAll(schema, args[0]);
    }


    // sample
    public static void gen1box_manyitems()
    {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        Property itemId = item.addIdProperty().getProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property boxId = item.addLongProperty("boxId").getProperty();
        ToMany boxToItem = box.addToMany(item, boxId);
        boxToItem.orderDesc(itemId);

    }


    // sample
    public static void gen1box1item()
    {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        item.addIdProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property itemId = box.addLongProperty("itemId").getProperty();
        box.addToOne(item, itemId);

    }
}
