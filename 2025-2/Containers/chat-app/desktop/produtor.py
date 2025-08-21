import asyncio
import websockets
import json

async def receive_messages(websocket, chatbox_name):
    """
    Função assíncrona para receber mensagens de um WebSocket.
    """
    try:
        async for message in websocket:
            print(f"[{chatbox_name}] Mensagem recebida: {message}")
    except websockets.exceptions.ConnectionClosed as e:
        print(f"[{chatbox_name}] Conexão fechada: {e.reason}")

async def send_message(websocket, message):
    """
    Função assíncrona para enviar uma mensagem para um WebSocket.
    """
    await websocket.send(message)
    print(f"Mensagem enviada: {message}")

async def chat_client():
    """
    Função principal do cliente de chat.
    """
    # URL dos servidores WebSocket.
    # Você precisará ajustar as URLs para o seu servidor.
    # Exemplo com um endereço dinâmico (não funciona em Python como no JS):
    # const server_address = ... # Você precisaria obter isso de outra forma em Python
    producer_url = "ws://organic-barnacle-96464g6pgppfpvw6-8080.app.github.dev/"
    consumer_url = "ws://organic-barnacle-96464g6pgppfpvw6-8081.app.github.dev/"

    try:
        # Cria as conexões de forma assíncrona
        async with websockets.connect(producer_url) as ws_producer, \
                   websockets.connect(consumer_url) as ws_consumer:

            print("Conectado aos servidores WebSocket.")

            # Inicia as tarefas para receber mensagens em segundo plano
            receive_producer_task = asyncio.create_task(receive_messages(ws_producer, "chatboxproducer"))
            receive_consumer_task = asyncio.create_task(receive_messages(ws_consumer, "chatboxconsumer"))

            # Exemplo de envio de uma mensagem
            message_to_send = "Olá do cliente Python!"
            await send_message(ws_producer, message_to_send)

            # Mantém o cliente rodando para receber mensagens
            await asyncio.sleep(60) # Roda por 60 segundos antes de fechar

            # Cancela as tarefas de recebimento
            receive_producer_task.cancel()
            receive_consumer_task.cancel()

    except Exception as e:
        print(f"Erro de conexão: {e}")

# Executa o cliente de chat
if __name__ == "__main__":
    asyncio.run(chat_client())
