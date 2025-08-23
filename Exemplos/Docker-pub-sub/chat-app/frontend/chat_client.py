import websocket
import json
import time

# Endereço do servidor WebSocket
WS_SERVER = "ws://localhost:8080/chat"   # ajuste se necessário

def on_open(ws):
    print("✅ Conexão aberta com o servidor WebSocket.")
    print("Digite suas mensagens abaixo. Para sair, digite 'exit'.")

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
        print("\nEncerrando cliente...")

    ws.close()

def on_message(ws, message):
    print(f"<<< Recebido do servidor: {message}")

def on_error(ws, error):
    print(f"⚠️ Erro: {error}")

def on_close(ws, close_status_code, close_msg):
    print("❌ Conexão fechada com o servidor WebSocket.")

if __name__ == "__main__":
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp(
        WS_SERVER,
        on_open=on_open,
        on_message=on_message,
        on_error=on_error,
        on_close=on_close
    )
    ws.run_forever()
