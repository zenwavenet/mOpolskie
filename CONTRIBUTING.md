# 🤝 Przewodnik kontrybutora

Dziękujemy za zainteresowanie rozwojem aplikacji **mOpolskie**! Ten przewodnik pomoże Ci w efektywnym współudziale w projekcie.

## 📋 Przed rozpoczęciem

- Przeczytaj plik `README.md` aby zrozumieć cel i funkcjonalność aplikacji
- Sprawdź otwarte `Issues` i `Pull Requests` aby uniknąć duplikowania pracy
- Upewnij się, że masz zainstalowane Android Studio oraz Kotlin

## 🚀 Jak zacząć

### 1. Sklonuj repozytorium

```bash
git clone https://github.com/zenwavenet/mOpolskie.git
cd mOpolskie
```

### 2. Konfiguracja środowiska

- Otwórz projekt w Android Studio
- Synchronizuj projekt z plikami Gradle
- Sprawdź czy wszystko się kompiluje: `./gradlew build`

### 3. Tworzenie nowej funkcjonalności

1. Utwórz nowy branch: `git checkout -b feature/nazwa-funkcjonalności`
2. Wprowadź zmiany zgodnie ze standardami kodowania
3. Przetestuj zmiany lokalnie
4. Utwórz Pull Request

## 🏗️ Standardy kodowania

### Kotlin

- Używaj konwencji nazewnictwa Kotlin
- Dokumentuj publiczne funkcje i klasy
- Używaj `lateinit` tylko gdy konieczne
- Preferuj `data class` dla modeli danych

### Android

- Stosuj Material Design guidelines
- Używaj ViewBinding zamiast findViewById
- Implementuj responsywne layouty
- Dbaj o accessibility

### Struktura commitów

```
typ(zakres): krótki opis

Dłuższy opis (opcjonalny)

Fixes #123
```

Typy commitów:

- `feat`: nowa funkcjonalność
- `fix`: poprawka błędu
- `docs`: dokumentacja
- `style`: formatowanie kodu
- `refactor`: refaktoring
- `test`: testy
- `chore`: aktualizacje zależności

## 🐛 Zgłaszanie błędów

### Przed zgłoszeniem

- Sprawdź czy błąd nie został już zgłoszony
- Upewnij się, że używasz najnowszej wersji

### Template zgłoszenia

```markdown
## Opis błędu

Krótki opis problemu

## Kroki do reprodukcji

1. Przejdź do...
2. Kliknij na...
3. Zobacz błąd

## Oczekiwane zachowanie

Co powinno się stać

## Screenshots

(jeśli dotyczy)

## Środowisko

- Wersja aplikacji:
- Android:
- Urządzenie:
```

## 💡 Propozycje ulepszeń

### Template propozycji

```markdown
## Opis funkcjonalności

Krótki opis propozycji

## Uzasadnienie

Dlaczego ta funkcjonalność jest potrzebna?

## Propozycja implementacji

Jak można to zrealizować?

## Alternatywy

Jakie są inne rozwiązania?
```

## 🧪 Testowanie

- Testuj na różnych rozmiarach ekranów
- Sprawdź kompatybilność z różnymi wersjami Android
- Przetestuj w trybie offline/online
- Upewnij się, że aplikacja jest dostępna (accessibility)

## 📝 Pull Requests

### Checklist przed utworzeniem PR

- [ ] Kod kompiluje się bez błędów
- [ ] Testy przechodzą
- [ ] Dokumentacja jest aktualna
- [ ] Commit messages są zgodne z konwencją
- [ ] PR ma jasny tytuł i opis

### Proces review

1. Automatyczne testy muszą przejść
2. Przynajmniej jeden maintainer musi zaaprobować
3. Wszystkie komentarze muszą być rozwiązane
4. Merge tylko po spełnieniu wszystkich warunków

## 📞 Kontakt

- GitHub Issues: dla błędów i propozycji
- Email: [bok@zenwave.net](mailto:bok@zenwave.net)

## 🏷️ Etykiety Issues

- `bug` - błędy do naprawienia
- `enhancement` - nowe funkcjonalności
- `documentation` - aktualizacje dokumentacji
- `good first issue` - łatwe zadania dla początkujących
- `help wanted` - potrzebna pomoc społeczności
- `priority: high` - wysokie priorytety

Dziękujemy za wkład w rozwój aplikacji mOpolskie! 🎉
