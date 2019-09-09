<template>
  <v-app :dark="dark">
    <v-toolbar dark :color="dark ? 'secondary' : 'primary'">

      <!-- 调色板 -->
      <v-btn icon @click.stop="dark = !dark">
        <v-icon>invert_colors</v-icon>
      </v-btn>
      <v-spacer/>

      <!-- 标题 -->
      <v-toolbar-title v-text="title"/>
      <v-spacer/>


      <!-- 组词方式 -->
      <v-toolbar-items>
        <!-- <v-btn :to="{ name: 'RandomLayout' }" flat>随机生成</v-btn> -->
        <!-- <v-btn :to="{ name: 'InputLayout' }" flat>手动输入</v-btn> -->
        <v-btn @click="showWays = true;" flat to="/random">随机生成</v-btn>
        <v-btn @click="showWays = false;" flat to="/input">手动输入</v-btn>
      </v-toolbar-items>
    </v-toolbar>

    <!-- 主内容区域 -->
    <!-- <router-view /> -->
    <!-- 随机组词区域 -->
    <v-container grid-list-lg>
      <transition name="component-fade" mode="out-in">
        <RandomLayout-vue v-if="showWays" :showWays="showWays" :wordsCards="wordsCards"
                          :dialogCommonContent="dialogCommonContent" :showCommonDialog="showCommonDialog"
                          :commonConfirmFlag="commonConfirmFlag"></RandomLayout-vue>
        <InputLayout-vue v-else :showWays="showWays"></InputLayout-vue>
      </transition>

      <!--词组卡 -->
      <v-layout row wrap>
        <transition-group name="list" tag="v-flex">
          <v-flex v-for="wordsCard in wordsCards" :key="wordsCard.id" xs12 sm6 offset-sm3>
            <v-card hover>
              <v-card-title primary-title>
                <div>
                  <div class="headline">{{ wordsCard.wordText }}</div>
                  <span class="grey--text">{{ wordsCard.createdTime }}</span>
                </div>
              </v-card-title>

              <v-card-actions>
                <v-btn
                  v-show="!wordsCard.isMaked"
                  @click="makewords(wordsCard)"
                  flat
                  color="primary">组词
                </v-btn>
                <v-btn @click="showDeleteCardDialog(wordsCard.id)" flat color="error">删除</v-btn>
                <v-spacer/>
                <v-btn v-show="wordsCard.isMaked" icon @click="wordsCard.showWords = !wordsCard.showWords">
                  <v-icon>{{ wordsCard.showWords ? 'keyboard_arrow_up' : 'keyboard_arrow_down' }}</v-icon>
                </v-btn>
              </v-card-actions>

              <v-slide-y-transition>
                <v-card-text v-show="wordsCard.showWords">
                  <v-expansion-panel popout expand>
                    <v-expansion-panel-content>
                      <template v-slot:header>
                        <span>二字词语</span>
                        <span class="text-xs-right mr-3">{{ twoWordsLength }}个</span>
                      </template>
                      <v-card>
                        <v-card-text>
                          <div v-if="twoWordsLength != 0">
                            <span class="mr-3" v-for="item in wordsCard.twoWords" :key="item">{{ item }}</span>
                          </div>
                          <div v-else>{{ noContent }}</div>
                        </v-card-text>
                      </v-card>
                    </v-expansion-panel-content>
                    <v-expansion-panel-content>
                      <template v-slot:header>
                        <span>三字词语</span>
                        <span class="text-xs-right mr-3">{{ threeWordsLength }}个</span>
                      </template>
                      <v-card>
                        <v-card-text>
                          <div v-if="threeWordsLength != 0">
                            <span class="mr-3" v-for="item in wordsCard.threeWords" :key="item">{{ item }}</span>
                          </div>
                          <div v-else>{{ noContent }}</div>
                        </v-card-text>
                      </v-card>
                    </v-expansion-panel-content>
                    <v-expansion-panel-content>
                      <template v-slot:header>
                        <span>四字词语</span>
                        <span class="text-xs-right mr-3">{{ fourWordsLength }}个</span>
                      </template>
                      <v-card>
                        <v-card-text>
                          <div v-if="fourWordsLength != 0">
                            <span class="mr-3" v-for="item in wordsCard.fourWords" :key="item">{{ noContent }}</span>
                          </div>
                          <div v-else>{{ noContent }}</div>
                        </v-card-text>
                      </v-card>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                </v-card-text>
              </v-slide-y-transition>
              <div class="absolute">
                <v-btn flat icon @click="showDeleteCardDialog(wordsCard.id)">
                  <v-icon small>delete</v-icon>
                </v-btn>
              </div>
            </v-card>
          </v-flex>
        </transition-group>
      </v-layout>
    </v-container>

    <!-- 组词中的对话框 -->
    <v-layout row justify-center>
      <v-dialog v-model="showMakingDialog" persistent max-width="290">
        <v-card color="primary" dark>
          <v-card-text>
            组词中...
            <v-progress-linear indeterminate color="white" class="mb-0"></v-progress-linear>
          </v-card-text>
        </v-card>
      </v-dialog>
    </v-layout>

    <!-- 通用组词提示框 -->
    <v-layout row justify-center>
      <v-dialog v-model="showCommonDialog" persistent max-width="290">
        <v-card>
          <v-card-title class="headline">错误：</v-card-title>
          <v-card-text>
            {{ dialogCommonContent }}
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary darken-1"
              flat
              @click="returnFocus"
              v-focus="commonConfirmFlag"
            >
              确定
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>

    <!-- 删除词组提示框 -->
    <v-layout row justify-center>
      <v-dialog v-model="showDeleteDialog" persistent max-width="290">
        <v-card>
          <v-card-title class="headline">提示：</v-card-title>
          <v-card-text>
            {{ dialogDeleteContent }}
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="error darken-1"
              flat
              @click.stop="deleteCard"
              v-focus="deleteConfirmFlag"
            >
              确定
            </v-btn>
            <v-btn
              color="info darken-1"
              flat
              @click.stop="returnFocus"
            >
              取消
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>

    <!-- 底部版权 -->
    <v-footer class="pa-3">
      <v-spacer></v-spacer>
      <div>&copy; {{ new Date().getFullYear() }} -- ZJM</div>
      <v-spacer></v-spacer>
    </v-footer>
  </v-app>
</template>

<script>

    // 加载组件
    import RandomLayoutVue from "./words/RandomLayout.vue";
    import InputLayoutVue from "./words/InputLayout.vue";

    export default {
        data: function () {
            return {
                dark: false,  // 是否暗黑主题
                showWays: true,  // 设置组词方式的显示，默认显示随机方式
                showMakingDialog: false,  // 组词中对话框开关
                showDeleteDialog: false,  // 删除对话框开关
                showCommonDialog: false,  // 通用提示对话框开关
                commonConfirmFlag: false,  // 弹出框聚焦标识
                deleteConfirmFlag: false,  // 弹出框聚焦标识
                title: this.$appConfig.titles.appTitle,  // 顶部导航条名称
                dialogCommonContent: '',  // 通用提示框内容
                dialogDeleteContent: this.$appConfig.DialogContent.delete,  // 重复提示框内容
                noContent: this.$appConfig.tips.noContent,  // 无内容提示框
                wordsCards: [],  // 一套字和词的实体对象列表
                selectWordsId: '',  // 当前选中词卡的的ID
                twoWordsLength: 0,  // 二字词长度
                threeWordsLength: 0,  // 三字词长度
                fourWordsLength: 0,  // 四字词长度
            };
        },
        created() {
            // 处理路由路径，只有是input时候跳转input，其他路径全部跳转random
            if (this.$route.path == '/input') {
                this.showWays = false;
            } else {
                this.$router.push('/random');
            }
        },
        components: {
            RandomLayoutVue,
            InputLayoutVue
        },
        directives: {
            focus: function (el) {
                el.focus();
            }
        },
        methods: {
            /**
             * 开始组词
             * @param  {object} wordsCard 为组词的词组卡
             * @return {boolean}          不满足条件提前终止执行返回false，成功返回true
             */
            makewords(wordsCard) {
                // 判断是否存在词卡，true表示不存重复词卡
                let repeatFlag = true;

                // 数据校验
                // 如果输入内容为空
                if (wordsCard.wordText == '') {
                    this.dialogCommonContent = this.$appConfig.tips.notRequired;
                    this.showCommonDialog = true;
                    this.commonConfirmFlag = true;
                    return false;
                }
                // 数据是含有非中文
                if (!this.$appConfig.textRule.test(wordsCard.wordText)) {
                    this.dialogCommonContent = this.$appConfig.tips.isNotChar;
                    this.showCommonDialog = true;
                    this.commonConfirmFlag = true;
                    return false;
                }
                // 超过最大限制
                if (wordsCard.wordText.length > this.$appConfig.maxWordLength) {
                    this.dialogCommonContent = this.$appConfig.tips.wordLengthMaxLimtit;
                    this.showCommonDialog = true;
                    this.commonConfirmFlag = true;
                    return false;
                }
                // 低过最小限制
                if (wordsCard.wordText.length < this.$appConfig.minWordLength) {
                    this.dialogCommonContent = this.$appConfig.tips.wordLengthMinLimtit;
                    this.showCommonDialog = true;
                    this.commonConfirmFlag = true;
                    return false;
                }

                // 判断是否已经存在对应选项卡
                this.wordsCards.forEach((item, index) => {
                    if (item.wordText == wordsCard.wordText && wordsCard.isMaked != item.isMaked) {
                        repeatFlag = false;
                        this.dialogCommonContent = this.$appConfig.DialogContent.repeat;
                        this.showCommonDialog = true;
                        this.commonConfirmFlag = true;
                        return false;
                    }
                });

                // 已经存在词卡就弹框退出
                if (!repeatFlag) return false;

                // 打开组词弹出层，提高用户体验
                this.showMakingDialog = true;

                // 发送请求进行处理
                let makeResult = this.$http
                    .get('/word/makewords?chars=' + wordsCard.wordText + '&id=' + wordsCard.id)
                    .then(({data}) => {
                        // 返回的数据装填到当前对象
                        wordsCard.twoWords = data.twoWords;
                        wordsCard.threeWords = data.threeWords;
                        wordsCard.fourWords = data.fourWords;

                        // 设置词长度
                        this.twoWordsLength = wordsCard.twoWords.length;
                        this.threeWordsLength = wordsCard.threeWords.length;
                        this.fourWordsLength = wordsCard.fourWords.length;

                        // 设置词组卡词语列表显示
                        wordsCard.showWords = true;
                        // 设置已经组过词
                        wordsCard.isMaked = true;

                        // 如果词组卡列表中没有id则是手动组词的方式，需要添加id和创建时间
                        if (!wordsCard.id) {
                            wordsCard.id = data.id;
                            wordsCard.createdTime = this.nowTime();
                            this.wordsCards.unshift(wordsCard);
                        }
                        this.showMakingDialog = false;
                        return true;
                    })
                    .catch(() => {
                        this.showMakingDialog = false;
                        this.dialogCommonContent = this.$appConfig.tips.UnknownError;
                        this.showCommonDialog = true;
                        this.commonConfirmFlag = true;
                        return false;
                    });
                return makeResult;
            },
            /**
             * 打开删除词组卡的对话框
             * @param  {String} id 词组卡id
             * @return {null}
             */
            showDeleteCardDialog(id) {
                // 打开提示框
                this.showDeleteDialog = true;
                this.deleteConfirmFlag = true;
                // 提交要删除的id
                this.selectWordsId = id;
            },
            /**
             * 删除词组卡
             * @return {null}
             */
            deleteCard() {
                // 关闭弹出框
                this.showDeleteDialog = false;
                this.deleteConfirmFlag = false;
                // 执行删除
                this.wordsCards.forEach((item, index) => {
                    if (item.id == this.selectWordsId) {
                        this.wordsCards.splice(index, 1);
                        return;
                    }
                });
            },
            /**
             * 当前页面没有操作时候参会
             * @return {[type]} [description]
             */
            returnFocus() {
                this.showDeleteDialog = false;
                this.showCommonDialog = false;
                this.deleteConfirmFlag = false;
                this.commonConfirmFlag = false;
            },
            /**
             * 返回当前时间
             * @return {string} 当前时间字符串，例：2019年9月7日 12:53:20
             */
            nowTime() {
                let time = new Date();
                let year = time.getFullYear();
                let mouth = time.getMonth() + 1;
                let day = time.getDate();
                let hour = time.getHours();
                let minute = time.getMinutes();
                let second = time.getSeconds();
                return year + '年' + mouth + '月' + day + '日 '
                    + this.check(hour) + ':' + this.check(minute) + ':' + this.check(second);
            },
            /**
             * 处理时间，个位数时间添加0
             * @param  {String} time 时分秒
             * @return {String}      带0的时分秒
             */
            check(time) {
                return time < 10 ? '0' + time : time;
            }
        }
    };
</script>

<style>
  .align-center {
    margin-top: 15px;
  }

  .absolute {
    position: absolute;
    top: 0;
    right: 0;
  }

  .component-fade-enter-active, .component-fade-leave-active {
    transition: opacity .3s ease;
  }

  .component-fade-enter, .component-fade-leave-to
    /* .component-fade-leave-active for below version 2.1.8 */
  {
    opacity: 0;
  }

  .list-enter, .list-leave-to {
    opacity: 0;
    transform: translateY(80px);
  }

  .list-enter-active, .list-leave-active {
    transition: all 1s;
  }
</style>
