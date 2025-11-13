import tkinter as tk
import serial
import threading
import time

# Change to your serial port (COMx for Windows or /dev/ttyUSBx for Linux)
SERIAL_PORT = 'COM3'
BAUD_RATE = 9600

class DHTApp:
    def __init__(self, root):
        self.root = root
        self.root.title("DHT22 Sensor GUI")
        self.root.geometry("300x150")
        
        self.temp_label = tk.Label(root, text="Temperature: -- °C", font=("Arial", 14))
        self.temp_label.pack(pady=10)

        self.hum_label = tk.Label(root, text="Humidity: -- %", font=("Arial", 14))
        self.hum_label.pack(pady=10)

        self.ser = serial.Serial(SERIAL_PORT, BAUD_RATE, timeout=1)
        self.read_serial()

    def read_serial(self):
        def loop():
            while True:
                try:
                    line = self.ser.readline().decode('utf-8').strip()
                    if line and ',' in line:
                        temp, hum = line.split(',')
                        self.temp_label.config(text=f"Temperature: {temp} °C")
                        self.hum_label.config(text=f"Humidity: {hum} %")
                except:
                    pass
                time.sleep(2)

        thread = threading.Thread(target=loop, daemon=True)
        thread.start()

if __name__ == "__main__":
    root = tk.Tk()
    app = DHTApp(root)
    root.mainloop()
