import os
import logging

import requests
from datetime import datetime
import time

import asyncio
from telegram import Bot

import threading
import concurrent.futures   

# 로그 출력하도록 설정
logging.basicConfig(level=logging.WARNING, format='%(message)s')

# 봇 생성 후 발급받은 토큰을 입력합니다.
bot_token = "6124169956:AAEzbKAqkDt3_e9FXJOs9ZdqzxfQcHILx9E"

# 메시지를 보낼 대상의 chat_id를 입력합니다.
chat_id = "1988503341"

# 메시지를 보내는 함수를 정의합니다.
async def send_message(msg):
    # Bot 객체를 생성합니다.
    bot = Bot(token=bot_token)

    await bot.send_message(chat_id=chat_id, text=msg)


# 바이낸스 USD-Futures API 엔드포인트
url = "https://fapi.binance.com/fapi/v1/klines"

# 모든 거래쌍 정보를 가져옴
response = requests.get("https://fapi.binance.com/fapi/v1/exchangeInfo").json()
symbols = []
for symbol in response["symbols"]:
    # USDT로 계산되는 거래쌍만 포함시킴
    if symbol["quoteAsset"] == "USDT":
        symbols.append(symbol["symbol"])

global top_n_symbols
#Last24시간 기준 변동률 Top n개 종목 추출 함수
def top_n_list():
    while True:
        try:
            # Top n개 종목을 저장할 리스트 생성
            global top_n_symbols
            top_n_symbols = []

            # 모든 거래쌍 정보를 가져옴
            response = requests.get("https://fapi.binance.com/fapi/v1/ticker/24hr").json()

            # 상승률을 기준으로 내림차순으로 정렬
            sorted_symbols = sorted(response, key=lambda x: float(x['priceChangePercent']), reverse=True)

            # Top n개 종목 이름 추출하여 배열에 저장
            for symbol_info in sorted_symbols:
                symbol = symbol_info['symbol']
                if symbol.endswith('USDT'):
                    top_n_symbols.append(symbol)

            top_n_symbols = top_n_symbols[:20]  
            logging.warning("Top " + str(len(top_n_symbols)) + " 리스트 업데이트 완료")

        except Exception as e:
            logging.warning("Top 리스트 업데이트 실패: ", e)

        time.sleep(600)    # 10분 간격 업데이트

#Top n개 종목 추출 함수 스레드 실행
top_n_list_thread = threading.Thread(target=top_n_list)
top_n_list_thread.start()   

def scalping_recommendation(base_time, base_price_percentage, base_volume_ratio):
    # 텔레그램으로 보낼 메세지 및 시간 초기화 
    base_msg = f"기준 : {base_time}, 기준 변동률 : {(base_price_percentage - 1) * 100:.2}%, 기준 배수 : {base_volume_ratio}배\n\n"
    msg = '🐳 ' + datetime.now().strftime("%Y-%m-%d(%a) %H시 %M분 %S초") + ' 🐳' + '\n' + base_msg
    detection = 0

    # 심볼 처리 함수
    def process_symbol(symbol):
        # 거래쌍의 n분봉 데이터를 가져옴
        params = {
            "symbol": symbol,
            "interval": base_time,  # 수정
            "limit": 2
        }

        try:
            response = requests.get(url, params=params).json()

            # 이전 시간과 마감 시간의 거래량 추출
            prev_volume = int(float(response[0][5]))
            close_volume = int(float(response[1][5]))

            # 이전 시간의 저가와 고가 추출
            low_price = float(response[1][3])
            high_price = float(response[1][2])

        except Exception:  # 종목 이름만 존재하는 경우 건너뛰기
            return None

        # 가격 n% 증가 조건 설정
        if high_price >= low_price * base_price_percentage:
            # 거래량이 n배 증가 조건 설정
            if close_volume >= prev_volume * base_volume_ratio and prev_volume >= 1:
                return f"종목 : {symbol}\n변동률 : {(high_price / low_price - 1) * 100 : >5.2f}%,  거래량 : {(close_volume / prev_volume) : 5.1f}배\n\n"
            
        return None

    # 병렬 처리
    with concurrent.futures.ThreadPoolExecutor() as executor:
        results = executor.map(process_symbol, top_n_symbols)

    # 결과 메시지 합치기
    for result in results:
        if result:
            msg += result
            detection = 1

    # 출력 및 텔레그램으로 메시지를 전송
    if detection:
        logging.warning(msg)
        asyncio.run(send_message(msg))

def DayTrading_recommendation(base_time, base_price_percentage, base_volume_ratio):
    # 텔레그램으로 보낼 메세지 및 시간 초기화 
    base_msg = f"기준 : {base_time}, 기준 변동률 : {(base_price_percentage - 1) * 100:.2}%, 기준 배수 : {base_volume_ratio}배\n\n"
    msg = '🚨 ' + datetime.now().strftime("%Y-%m-%d(%a) %H시 %M분 %S초") + ' 🚨' + '\n' + base_msg
    detection = 0

    # 심볼 처리 함수
    def process_symbol(symbol):
        # 거래쌍의 n분봉 데이터를 가져옴
        params = {
            "symbol": symbol,
            "interval": base_time,  # 수정
            "limit": 3
        }

        try:
            response = requests.get(url, params=params).json()

            # 이전 시간과 마감 시간의 거래량 추출
            prev_volume = int(float(response[0][5]))
            close_volume = int(float(response[1][5]))

            # 이전 시간의 저가와 고가 추출
            low_price = float(response[1][3])
            high_price = float(response[1][2])

        except Exception:  # 종목 이름만 존재하는 경우 건너뛰기
            return None

        # 가격 n% 증가 조건 설정
        if high_price >= low_price * base_price_percentage:
            # 거래량이 n배 증가 조건 설정
            if close_volume >= prev_volume * base_volume_ratio and prev_volume >= 1:
                return f"종목 : {symbol}\n변동률 : {(high_price / low_price - 1) * 100 : >5.2f}%,  거래량 : {(close_volume / prev_volume) : 5.1f}배\n\n"

        return None

    # 병렬 처리
    with concurrent.futures.ThreadPoolExecutor() as executor:
        results = executor.map(process_symbol, symbols)

    # 결과 메시지 합치기
    for result in results:
        if result:
            msg += result
            detection = 1

    # 출력 및 텔레그램으로 메시지를 전송
    if detection:
        logging.warning(msg)
        asyncio.run(send_message(msg))

def BTC_recommendation(base_time, base_price_percentage, base_volume_ratio):
    # 텔레그램으로 보낼 메세지 및 시간 초기화 
    base_msg = f"기준 : {base_time}, 기준 변동률 : {(base_price_percentage - 1) * 100:.2}%, 기준 배수 : {base_volume_ratio}배\n\n"
    msg = '💥 ' + datetime.now().strftime("%Y-%m-%d(%a) %H시 %M분 %S초") + ' 💥' + '\n' + base_msg
    detection = 0

    # 거래쌍의 n분봉 데이터를 가져옴
    params = {
        "symbol": "BTCUSDT",
        "interval": base_time,       #수정
        "limit": 3
    }
        
    response = requests.get(url, params=params).json()

    # 이전 시간과 마감 시간의 거래량 추출
    prev_volume = int(float(response[0][5]))
    close_volume = int(float(response[1][5]))

    # 이전 시간의 저가와 고가 추출
    low_price = float(response[1][3])
    high_price = float(response[1][2])

    # 가격 n% 증가 조건 설정
    if high_price >= low_price * base_price_percentage:       #수정

        # 거래량이 n배 증가 조건 설정
        if close_volume >= prev_volume * base_volume_ratio and prev_volume >= 1:        #수정       
            msg = msg + f"종목 : BTCUSDT\n변동률 : {(high_price / low_price - 1) * 100 : >5.2f}%,  거래량 : {(close_volume / prev_volume) : 5.1f}배\n\n"      
            detection = 1
    
    # 출력 및 텔레그램으로 메시지를 전송
    if detection:
        logging.warning(msg)
        asyncio.run(send_message(msg))

def increasing_recommendation(base_time, base_price_percentage, base_volume_ratio):
    # 텔레그램으로 보낼 메세지 및 시간 초기화 
    base_msg = f"기준 : {base_time}, 기준 상승률 : {(base_price_percentage - 1) * 100:.2}%, 기준 배수 : {base_volume_ratio}배\n\n"
    msg = '🚨 ' + datetime.now().strftime("%Y-%m-%d(%a) %H시 %M분 %S초") + ' 🚨' + '\n' + base_msg
    detection = 0

    # 심볼 처리 함수
    def process_symbol(symbol):
        # 거래쌍의 n분봉 데이터를 가져옴
        params = {
            "symbol": symbol,
            "interval": base_time,  # 수정
            "limit": 3
        }
        
        try:
            response = requests.get(url, params=params).json()

            # 이전 시간과 마감 시간의 거래량 추출
            prev_volume = int(float(response[0][5]))
            close_volume = int(float(response[1][5]))

            # 이전 시간과 마감 시간의 가격 추출
            start_price = float(response[1][1])
            close_price = float(response[1][4])

        except Exception:  # 종목 이름만 존재하는 경우 건너뛰기
            return None

        # 가격 n% 증가 조건 설정
        if close_price >= start_price * base_price_percentage:
            # 거래량이 n배 증가 조건 설정
            if close_volume >= prev_volume * base_volume_ratio and prev_volume >= 1:
                return f"종목 : {symbol}\n상승률 : {(close_price / start_price - 1) * 100 : >5.2f}%,  거래량 : {(close_volume / prev_volume) : 5.1f}배\n\n"

        return None

    # 병렬 처리
    with concurrent.futures.ThreadPoolExecutor() as executor:
        results = executor.map(process_symbol, symbols)

    # 결과 메시지 합치기
    for result in results:
        if result:
            msg += result
            detection = 1

    # 출력 및 텔레그램으로 메시지를 전송
    if detection:
        logging.warning(msg)
        asyncio.run(send_message(msg))


# 메인 함수 부분
while True:
    try:
        # n분 대기
        time.sleep(60 - time.time() % 60)      # 대기시간

        if int(time.time() % 14400) == 0:       # 4시간 간격
            scalping_recommendation("5m", 1.01, 2)
            time.sleep(5)       # 서버 정보 업데이트 대기 시간
            BTC_recommendation("1h", 1.01, 2)
            DayTrading_recommendation("1h", 1.03, 2)
            DayTrading_recommendation("4h", 1.05, 2)
        elif int(time.time() % 3600) == 0:      # 1시간 간격
            scalping_recommendation("5m", 1.01, 2)
            time.sleep(5)       # 서버 정보 업데이트 대기 시간
            BTC_recommendation("1h", 1.008, 2)
            DayTrading_recommendation("1h", 1.03, 2)
        elif int(time.time() % 300) == 0:      # 5분 간격
            scalping_recommendation("5m", 1.01, 2)

    except KeyboardInterrupt:
        os._exit(0)

    except Exception as e:
        logging.warning("오류가 발생했습니다:", e)
        continue  # 다시 반복하여 프로그램 재실행