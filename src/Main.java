import ra.bussiness.ShopBussiness;
import ra.entity.Account;
import ra.entity.Bill;
import ra.entity.BillDetail;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Account accLogin = null;
        do {
            System.out.println("Nhập userName:");
            String userName = scanner.nextLine();
            System.out.println("Nhập password:");
            String password = scanner.nextLine();
            //Login
            accLogin = ShopBussiness.login(userName, password);
            if (accLogin == null) {
                System.err.println("Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
            }
        } while (accLogin == null);
        //Lưu thông tin acc ra file
        writeToFile(accLogin);
        //Chuyển hướng sang menu admin hoặc user
        if (accLogin.isPermission()) {
            menuUser(scanner);
        } else {
            menuAdmin(scanner);
        }
    }

    public static void menuAdmin(Scanner scanner) {
        do {
            System.out.println("***************MENU******************");
            System.out.println("1. Tạo phiếu nhập");
            System.out.println("2. Duyệt phiếu nhập");
            System.out.println("3. Tạo phiếu xuất");
            System.out.println("4. Duyệt phiếu xuất");
            System.out.println("5. Thoát");
            System.out.print("Lựa chọn của bạn:");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    boolean result = Main.createImportBill(scanner);
                    if (result){
                        System.out.println("Tạo phiếu nhập thành công");
                    }else{
                        System.err.println("Tạo phiếu nhập thất bại");
                    }
                    break;
                case 2:
                    if(Main.authImportBill(scanner)){
                        System.out.println("Hoàn tất quá trình duyệt");
                    }else{
                        System.err.println("Có lỗi xảy ra trong quá trình duyệt");
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.err.println("Vui lòng chọn từ 1-5");
            }
        } while (true);
    }

    public static void menuUser(Scanner scanner) {

    }

    public static boolean createImportBill(Scanner scanner) {
        //1. Tạo phiếu nhập
        Bill importBill = new Bill();
        System.out.println("Nhập vào mã phiếu nhập:");
        importBill.setBillCode(scanner.nextLine());
        importBill.setBillType(true);
        Account accLogin = readFromFile();
        importBill.setEmpIdCreated(accLogin.getEmp_id());
        int importBillId = ShopBussiness.createImportBill(importBill);
        if (importBillId < 0) {
            System.err.println("Tạo mới bill thất bại");
        } else {
            //2. Tạo các phiếu nhập chi tiết
            System.out.println("Bạn có muốn tạo phiếu nhập chi tiết không?");
            System.out.println("1. Có");
            System.out.println("2. Không");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                //Tạo các bill chi tiết
                boolean isExit = true;
                List<BillDetail> listBillDetail = new ArrayList<>();
                do {
                    BillDetail billDetail = new BillDetail();
                    System.out.println("Nhập vào mã sản phẩm nhập:");
                    billDetail.setProductId(scanner.nextLine());
                    System.out.println("Nhập số lượng nhập:");
                    billDetail.setQuantity(Integer.parseInt(scanner.nextLine()));
                    System.out.println("Nhập giá nhập:");
                    billDetail.setPrice(Float.parseFloat(scanner.nextLine()));
                    billDetail.setBillId(importBillId);
                    listBillDetail.add(billDetail);
                    System.out.println("Bạn có muốn nhập phiếu nhập chi tiết nữa không?");
                    System.out.println("1. Có");
                    System.out.println("2. Không");
                    int choiceDetail = Integer.parseInt(scanner.nextLine());
                    if (choiceDetail != 1) {
                        isExit = false;
                    }
                } while (isExit);
                //Thêm các phiếu nhập chi tiết vào CSDL
                for (BillDetail billDetail : listBillDetail) {
                    boolean result = ShopBussiness.createImportBillDetail(billDetail);
                    if (!result){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean authImportBill(Scanner scanner){
        System.out.println("Nhập vào code của phiếu nhập cần duyệt:");
        String billCode = scanner.nextLine();
        //Tiến hành duyệt
        return ShopBussiness.authBillCode(billCode);
    }

    public static void writeToFile(Account account) {
        File file = new File("userLogin.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(account);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Account readFromFile() {
        File file = new File("userLogin.txt");
        Account account = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            account = (Account) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return account;
    }
}
