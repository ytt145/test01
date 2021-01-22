package j200212;

import java.net.*; // ServerSocket, Socket
import java.io.*;  // �����

// ���� �迭, ������ Ŭ���̾�Ʈ�� ������ �ǽð����� �����ϴ� ����(���� �迭X)
import java.util.Vector;

public class ChatGUIServer {

    // Ŭ���̾�Ʈ�� ������ ���� �ʿ��� ServerSocket Ŭ����
    ServerSocket ss;

    // ������ ������ Ŭ���̾�Ʈ Socket�� ������ ��� ����
    Socket s;

    // ���� Ŭ���̾�Ʈ ���� �ǽð� ����
    Vector v;

    // ServerThread �ڷ��� ��� ���� ����, has-a ���� ������ ����
    ServerThread st;

    // ������, ��� ���� �ʱ�ȭ
    public ChatGUIServer() {
        // ����� ������ ���� v�� Vector ��ü�� �ʱ�ȭ
        v = new Vector();

        // ������ �� ���� �ְ� �� �� ���� �ֱ� ������ ���� ó��
        try {
            // ServerSocket ��ü ���� �� ��Ʈ ��ȣ ����(������ ��ȣ �ο�)
            ss = new ServerSocket(5432);
            System.out.println("ss>>>" + ss);
            System.out.println("ä�� ���� ������...");

            // ���� ����: Ŭ���̾�Ʈ�� ������ ������ ��ٸ��� ��(���� ���)
            while (true) {
                // ���� Ŭ���̾�Ʈ Socket�� s ������ ����
                s = ss.accept();
                System.out.println("Accepted from" + s);

                // ���� Ŭ���̾�Ʈ�� ������ st��ü ����
                st = new ServerThread(this, s);

                // ������ ������ v�� ���� Ŭ���̾�Ʈ ������ �߰�
                this.addThread(st);

                // Thread ���� -> run() -> broadCast() -> send() �ǽð� �޼ҵ� ȣ��
                st.start();
            }

        } catch (Exception e) {
            // ���� ���н� ������ Error �޼��� ���
            System.out.println("���� ���� ����>>>" + e);
        }
    }

    // ���� v�� ���� Ŭ���̾�Ʈ�� ������ ����
    public void addThread(ServerThread st) {
        v.add(st);
    }

    // ������ Ŭ���̾�Ʈ ������ ����
    public void removeThread(ServerThread st) {
        v.remove(st);
    }

    // �� Ŭ���̾�Ʈ���� �޼����� ����ϴ� �޼ҵ�, send() ȣ��
    public void broadCast(String str) {
        for (int i = 0; i < v.size(); i++) {
            // ������ Ŭ���̾�Ʈ�� ServerThread ��ü�� �� ��ȯ 
            ServerThread st = (ServerThread) v.elementAt(i);

            // �� ������ ��ü�� str ���ڿ��� ����
            st.send(str);
        }
    }

    public static void main(String[] args) {

        // �͸� ��ü ����
        new ChatGUIServer();

    }

}

// ServerThread Ŭ���� ���� �� �������� �� Ŭ���̾�Ʈ�� ��û�� ó���� ������
class ServerThread extends Thread {

    // Ŭ���̾�Ʈ ���� ����
    Socket s;

    // ChatGUIServer Ŭ������ ��ü�� ��� ������ ����, has-a ���踦 ����
    ChatGUIServer cg;

    // �����
    BufferedReader br;
    PrintWriter pw;

    // ������ ���ڿ�
    String str;

    // ��ȭ��(ID)
    String name;

    // ������
    public ServerThread(ChatGUIServer cg, Socket s) {
        /* cg = new ChatGUIServer(); �� �ۼ� �Ұ�, ������ �� �� �����Ǳ� ������ �浹�� �Ͼ
        ���� �Ű������� �̿��ؼ� ��ü�� ����(call by reference) �ڿ� cg�� s���� �ʱ�ȭ�ؾ� ��
        */
        this.cg = cg;

        // ������ Ŭ���̾�Ʈ ���� ����
        this.s = s;

        // ������ ������ ���� ����� ��Ʈ�� ����
        try {
            // =========== �Է� ===========
            // s.getInputStream() => ���� Ŭ���̾�Ʈ(���� ��ü)�� InputStream�� ��� ��
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            // =========== ��� ===========
            /*
            BufferedWriter�� ��� ���۸� ����� ������ ������ PrintWriter ��Ʈ�� ���
            PrintWriter ��Ʈ���� ��� �������� �� ��° ���ڷ� autoFlush ����� ������ �� ����
            BufferedWriter�� ����ϴ� ��� flush() �޼ҵ带 ����ؾ� ��
            */
            pw = new PrintWriter(s.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("���� �߻�>>>>>" + e);
        }
    }

    // �޼���(�Է� ���ڿ�) ��� �޼ҵ�
    public void send(String str) {
        // ���ڿ� ���
        pw.println(str);

        // Ȥ�ó� ���ۿ� �����ִ� ���� �����
        pw.flush();
    }

    // run()_ServerThread -> broadCast(str)_ChatGUIServer -> send(str)_ServerThread
    public void run() {
        try {
            // ��ȭ�� �Է� �ޱ�
            pw.println("��ȭ���� �Է��ϼ���");
            name = br.readLine();

            // �������� �� Ŭ���̾�Ʈ�� ��ȭ�� ���
            cg.broadCast("[" + name + "]" + "���� �����߽��ϴ�.");

            // ���� ����ϸ� �Է��� �޼����� �� Ŭ���̾�Ʈ�� ��� ����
            while ((str = br.readLine()) != null) {
                cg.broadCast("[" + name + "]: " + str);
            }
        } catch (Exception e) {
            // ������ ����� v���� �ش� Ŭ���̾�Ʈ ������ ����
            cg.removeThread(this); // this: ServerThread ��ü, ���� Ŭ���̾�Ʈ
             // �������� �� Ŭ���̾�Ʈ�� ���
            cg.broadCast("[" + name + "]" + "���� �����߽��ϴ�.");

            // �ֿܼ� ���� Ŭ���̾�Ʈ IP �ּ� ���
            System.out.println(s.getInetAddress() + "�� ������ �����!");
        }
    }

}