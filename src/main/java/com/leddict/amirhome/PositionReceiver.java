package com.leddict.amirhome;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PositionReceiver extends Thread {

    public int getPositionMs() {
        int currentPosition;
        m_lastPositionMutex.lock();
        currentPosition = m_lastPosition;
        m_lastPositionMutex.unlock();
        return currentPosition;
    }

    public void run(){

        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(null);
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(2001));
        }
        catch (java.net.SocketException e) {
            System.out.println("excpetion in socket creation " + e.getMessage());
            System.exit(1);
        }
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        PositionReport.PositionReportMsg positionReportMsg = null;

        while(true) {
            try {
                serverSocket.receive(receivePacket);
                byte[] msgBytes = new byte[receivePacket.getLength()];
                for (int i = 0; i < receivePacket.getLength(); i++) {
                    msgBytes[i] = receiveData[i];
                }
                positionReportMsg = PositionReport.PositionReportMsg.parseFrom(msgBytes);
                m_lastPositionMutex.lock();
                m_lastPosition = positionReportMsg.getSongs(0).getPositionInMs();
                m_lastPositionMutex.unlock();
            }
            catch (java.io.IOException e) {
                System.out.println("io excpetion: " + e.getMessage());
                System.exit(1);
            }
        }

    }

    private int m_lastPosition = 0;
    private final Lock m_lastPositionMutex = new ReentrantLock(true);

}
