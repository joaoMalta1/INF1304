import websocket
import json
import time

# Lista de servidores WebSocket disponíveis
WS_SERVER = [
    "ws://localhost:8080/chat/ws",
    "ws://localhost:8180/chat/ws"
]

def chat_loop(ws):
    print("[INFO] Conexão aberta com o servidor WebSocket.")

    try:
        while True:
            user = input("Usuário: ").strip()
            if user.lower() == "exit":
                break

            message = input("Mensagem: ").strip()
            if message.lower() == "exit":
                break

            msg = {
                "user": user,
                "message": message,
                "timestamp": time.time()
            }

            ws.send(json.dumps(msg))
            print(f">>> Enviado: {msg}")

    except KeyboardInterrupt:
        print("\n[INFO] Encerrando cliente...")

    ws.close()

def on_open(ws):
    chat_loop(ws)

def on_message(ws, message):
    print(f"<<< Recebido do servidor: {message}")

def on_error(ws, error):
    print(f"[ERROR] Erro: {error}")

def on_close(ws, close_status_code, close_msg):
    print("[INFO] Conexão fechada com o servidor WebSocket.")

def connect():
    server_index = 0
    while True:
        server = WS_SERVER[server_index]
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
        server_index = (server_index + 1) % len(WS_SERVER)
        time.sleep(2)

if __name__ == "__main__":
    websocket.enableTrace(False)
    connect()
