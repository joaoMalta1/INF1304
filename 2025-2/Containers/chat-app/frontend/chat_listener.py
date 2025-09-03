import websocket
import time


WS_SERVERS = [
    "ws://localhost:8081/chat/ws",
    "ws://localhost:8181/chat/ws"
]

def on_open(ws):
    print("[INFO] Conectado ao ChatConsumer (Java).")
    print("Aguardando mensagens de broadcast...")
    return

def on_message(ws, message):
    print(f"<<< Mensagem recebida: {message}")
    return

def on_error(ws, error):
    print(f"[ERROR] Erro: {error}")
    return

def on_close(ws, close_status_code, close_msg):
    print("[INFO] Conexão fechada.")
    return

def connect():
    server_index = 0
    while True:
        server = WS_SERVERS[server_index]
        print(f"conectar: {server}...")
        try:
            ws = websocket.WebSocketApp(
                server,
                on_open=on_open,
                on_message=on_message,
                on_error=on_error,
                on_close=on_close
            )
            ws.run_forever()
        except Exception as e:
            print(f"deu ruim ao conectar no server {server}: {e}")
        server_index = (server_index + 1) % len(WS_SERVERS)
        time.sleep(2)

if __name__ == "__main__":
    connect()