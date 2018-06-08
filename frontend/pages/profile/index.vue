<template>
    <v-container>
        <v-card>
            <v-card-title>
                <div class="title" style="width: 100%;">Добро пожаловать, {{ user.username }}!</div>
                <div class="grey--text body2 mt-2">На этой странице вы можете изменить настройки своего профиля.</div>
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text>
                <div class="title">Персональные данные</div>
                <v-layout row justify-center>
                    <v-flex xs12 md8 lg6>
                        <v-form ref="personalDataForm" @submit.native.prevent="updatePersonalData">
                            <v-text-field
                                v-model="email"
                                append-icon="alternate_email"
                                label="E-mail"
                                hint="Укажите действующий адрес электронной почты"
                                required :rules="[required, validEmail]"
                            ></v-text-field>
                            <v-text-field
                                v-model="phone"
                                label="Телефон"
                                prefix="8"
                                append-icon="phone"
                                mask="(###) ###-##-##"
                                hint="Ваш мобильный телефон"
                                required :rules="[required, validPhone]"
                            ></v-text-field>

                            <v-layout>
                                <v-spacer></v-spacer>
                                <v-btn dark color="primary" type="submit" :loading="loading.data">
                                    <v-icon left>save</v-icon>
                                    Сохранить
                                </v-btn>
                            </v-layout>
                        </v-form>
                    </v-flex>
                </v-layout>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-text>
                <div class="title">Сменить пароль</div>
                <v-layout row justify-center>
                    <v-flex xs12 md8 lg6>
                        <v-form ref="passwordForm" @submit.native.prevent="changePassword">
                            <v-text-field
                                v-model="oldPassword"
                                prepend-icon="lock_open"
                                :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                :append-icon-cb="() => { passwordVisible = !passwordVisible; }"
                                :type="passwordVisible ? 'password' : 'text'"
                                label="Текущий пароль"
                                required :rules="[required, long]"
                            ></v-text-field>
                            <v-text-field
                                v-model="password"
                                prepend-icon="lock"
                                :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                :append-icon-cb="() => { passwordVisible = !passwordVisible; }"
                                :type="passwordVisible ? 'password' : 'text'"
                                label="Новый пароль"
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

                            <v-layout>
                                <v-spacer></v-spacer>
                                <v-btn dark color="primary" type="submit" :loading="loading.password">
                                    <v-icon left>done</v-icon>
                                    Изменить пароль
                                </v-btn>
                            </v-layout>
                        </v-form>
                    </v-flex>
                </v-layout>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-text>
                <div class="title error--text">Удаление аккаунта</div>
                <v-layout column align-center>
                    <div class="body-1 grey--text text--darken-2 mt-3" style="align-self: flex-start;">
                        Вы можете удалить свой аккаунт и все связанные с ними данные.
                        Восстановление удалённых данных невозможно.
                    </div>
                    <v-dialog
                        v-model="deleteDialog"
                        max-width="400"
                    >
                        <v-btn outline large color="error" slot="activator">
                            <v-icon left>delete_forever</v-icon>
                            Удалить аккаунт
                        </v-btn>

                        <v-card>
                            <v-card-title class="title">
                                <v-icon large color="error">warning</v-icon>
                                <span class="ml-3">Подтвердите удаление</span>
                            </v-card-title>
                            <v-card-text>
                                <div>Введите пароль, чтобы продолжить.</div>

                                <v-text-field
                                    v-model="deletePassword"
                                    label="Пароль"
                                    prepend-icon="lock_open"
                                    :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                    :append-icon-cb="() => { passwordVisible = !passwordVisible; }"
                                    :type="passwordVisible ? 'password' : 'text'"
                                ></v-text-field>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn
                                    dark color="error"
                                    :loading="loading.removal"
                                    @click="removeAccount"
                                ><v-icon>delete_forever</v-icon>Удалить</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </v-layout>
            </v-card-text>
        </v-card>
    </v-container>
</template>
<script>
import {mapGetters, mapActions} from 'vuex';

export default {
    name: 'ProfilePage',
    middleware: 'auth',

    data() {
        return {
            email: this.$store.state.user.user.email,
            phone: this.$store.state.user.user.phone,

            oldPassword: '',
            password: '',
            confirmPassword: '',
            passwordVisible: false,

            loading: {
                data: false,
                password: false,
                removal: false
            },

            deleteDialog: false,
            deletePassword: ''
        };
    },

    computed: {
        ...mapGetters({
            user: 'user/user'
        })
    },
    methods: {
        ...mapActions({
            getMe: 'user/getMe'
        }),

        required(v) { return !!v || 'Это обязательное поле'; },
        allowedUsername(v) { return (!!v && /^[A-Za-z0-9_\-.]+$/.test(v)) || 'Имя пользователя содержит недопустимые символы'; },
        long(v) { return (!!v && v.length >= 5) || 'Пароль слишком короткий!'; },
        agree(v) { return v || 'Для продолжения регистрации необходимо согласие с условиями использования'; },
        validEmail(v) { return (!!v && /^.+@.+\..+$/.test(v)) || 'Введите корректный адрес электронной почты'; },
        validPhone(v) { return (!!v && /^[0-9]{10,10}$/.test(v)) || 'Введите корректный номер телефона'; },
        passwordsMatch(v) {
            return v === this.password || 'Пароли не совпадают!';
        },

        async updatePersonalData() {
            if (this.$refs.personalDataForm.validate()) {
                this.loading.data = true;
                try {
                    await this.$store.dispatch('user/updatePersonalData', {
                        phone: this.phone,
                        email: this.email
                    });
                    this.$store.commit('showSuccess');
                } catch (e) {
                    console.error(e);
                    this.$store.commit('showError', { error: e, text: 'Не удалось обновить информацию профиля.' });
                }
                this.loading.data = false;
            }
        },
        async changePassword() {
            if (this.$refs.passwordForm.validate()) {
                this.loading.password = true;
                try {
                    await this.$store.dispatch('user/changePassword', { password: this.password });
                    this.$store.commit('showSuccess');
                } catch (e) {
                    console.error(e);
                    this.$store.commit('showError', { error: e, text: 'Не удалось сменить пароль.' });
                }
                this.loading.password = false;
            }
        },

        async removeAccount() {
            this.loading.removal = true;
            try {
                await this.$store.dispatch('user/removeAccount', { password: this.deletePassword });
            } catch (e) {
                console.error(e);
                this.$store.commit('showError', { error: e, text: 'Ошибка при удалении:' });
            }
            this.deleteDialog = false;
            this.deletePassword = '';
            this.loading.removal = false;
        }
    }
};
</script>
