<template>
    <v-layout row>
        <v-flex
            xs12
            md8 offset-md2
            lg6 offset-lg3
            xl4 offset-xl4
            class="pt-3"
        >
            <v-card>
                <v-tabs
                    v-model="tab"
                    slider-color="primary"
                    grow
                >
                    <v-tab key="login">Вход</v-tab>
                    <v-tab key="register">Регистрация</v-tab>
                </v-tabs>
                <v-card-text>
                    <v-tabs-items v-model="tab">
                        <v-tab-item
                            key="login"
                        >
                            <v-form ref="loginForm" @submit.native.prevent="submitLogin">
                                <v-text-field
                                    v-model="username"
                                    prepend-icon="person"
                                    label="Имя пользователя"
                                    required :rules="[required, allowedUsername]"
                                ></v-text-field>
                                <v-text-field
                                    v-model="password"
                                    prepend-icon="lock"
                                    :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                    :append-icon-cb="() => { passwordVisible = !passwordVisible; }"
                                    :type="passwordVisible ? 'password' : 'text'"
                                    label="Пароль"
                                    required :rules="[required, long]"
                                ></v-text-field>
                                <v-layout row>
                                    <v-spacer></v-spacer>
                                    <v-btn type="submit" large dark color="primary">
                                        <v-icon left>input</v-icon>
                                        Вход
                                    </v-btn>
                                </v-layout>
                            </v-form>
                        </v-tab-item>
                        <v-tab-item
                            key="register"
                        >
                            <v-form ref="registerForm" @submit.native.prevent="submitRegister">
                                <v-text-field
                                    v-model="email"
                                    prepend-icon="alternate_email"
                                    label="E-mail"
                                    hint="Укажите действующий адрес электронной почты"
                                    required :rules="[required, validEmail]"
                                ></v-text-field>
                                <v-text-field
                                    v-model="username"
                                    prepend-icon="person"
                                    label="Имя пользователя"
                                    hint="Придумайте имя пользователя"
                                    required :rules="[required, allowedUsername]"
                                ></v-text-field>
                                <v-text-field
                                    v-model="password"
                                    prepend-icon="lock"
                                    :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                    :append-icon-cb="() => { passwordVisible = !passwordVisible; }"
                                    :type="passwordVisible ? 'password' : 'text'"
                                    label="Пароль"
                                    hint="Не менее 5 символов"
                                    required :rules="[required, long]"
                                ></v-text-field>
                                <v-text-field
                                    v-model="confirmPassword"
                                    prepend-icon="lock"
                                    :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                    :append-icon-cb="() => { passwordVisible = !passwordVisible; }"
                                    :type="passwordVisible ? 'password' : 'text'"
                                    label="Подтвердите пароль"
                                    required :rules="[required, v => passwordsMatch(v)]"
                                ></v-text-field>
                                <v-checkbox
                                    v-model="agreement"
                                    color="primary"
                                    label="Я согласен с условиями использования сервиса"
                                    required :rules="[agree]"
                                ></v-checkbox>
                                <v-layout row>
                                    <v-spacer></v-spacer>
                                    <v-btn type="submit" large dark color="primary">
                                        <v-icon left>person_add</v-icon>
                                        Регистрация
                                    </v-btn>
                                </v-layout>
                            </v-form>
                        </v-tab-item>
                    </v-tabs-items>
                </v-card-text>
            </v-card>
        </v-flex>
    </v-layout>
</template>
<script>
export default {
    name: 'LoginPage',
    data() {
        let tab = '0';
        if (this.$route.query && this.$route.query.tab === 'register') {
            tab = '1';
        }
        return {
            tab,

            username: '',
            password: '',
            passwordVisible: true,

            confirmPassword: '',
            email: '',
            agreement: false,

            loading: false
        };
    },

    methods: {
        submitLogin() {
            if (this.$refs.loginForm.validate()) {
                // TODO: login
            }
        },
        submitRegister() {
            if (this.$refs.registerForm.validate()) {
                // TODO: register
            }
        },

        required(v) { return !!v || 'Это обязательное поле'; },
        allowedUsername(v) { return (!!v && /^[A-Za-z0-9_\-.]+$/.test(v)) || 'Имя пользователя содержит недопустимые символы'; },
        long(v) { return (!!v && v.length >= 5) || 'Пароль слишком короткий!'; },
        agree(v) { return v || 'Для продолжения регистрации необходимо согласие с условиями использования'; },
        validEmail(v) { return (!!v && /^.+@.+\..+$/.test(v)) || 'Введите корректный адрес электронной почты'; },
        passwordsMatch(v) {
            return v === this.password || 'Пароли не совпадают!';
        }
    },

    watch: {
        tab(nval, oval) {
            if (nval !== oval) {
                this.$router.push({
                    query: {
                        tab: nval === '1' ? 'register' : 'login'
                    }
                });
            }
        }
    }
};
</script>
